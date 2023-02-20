package com.rolldebee.sqlgen.red

import com.github.javafaker.Faker
import com.rolldebee.sqlgen.core.*
import org.apache.commons.math3.random.RandomDataGenerator

class ForeignKeyConstraintGenerator(
    private val tables: List<Table>,
    private val foreignKeyMaxCount: Int,
) : Generator {
    override fun generate(): List<Renderer> {
        val randomDataGenerator = RandomDataGenerator()
        val renderers: ArrayList<Renderer> = ArrayList()
        val foreignKeyConstraints: ArrayList<ForeignKeyConstraint> = ArrayList()
        for (table in tables) {
            val usedForeignTables: ArrayList<Table> = ArrayList()
            val foreignKeyCount = randomDataGenerator.nextInt(1, foreignKeyMaxCount)
            for (i in 0 until foreignKeyCount) {
                val usableTables: List<Table> = tables
                    .filter { e -> e.foreignKeyConstraints.none { x -> x.foreignColumn.table == table } }
                    .filter { e -> e != table }
                    .filter { e -> usedForeignTables.none { x -> x == e } }
                if (usableTables.isEmpty()) {
                    continue
                }
                val foreignTable: Table = usableTables[randomDataGenerator.nextInt(0, usableTables.size - 1)]
                val foreignTableColumn =
                    foreignTable.columns.firstOrNull { e -> e.name.lowercase() == "id" } ?: continue
                usedForeignTables.add(foreignTable)
                val column = Column(
                    name = suffix(foreignTable.name, "_id"),
                    foreignColumn = foreignTableColumn,
                    columnType = foreignTableColumn.columnType,
                    table = table
                )
                table.columns.add(column)
                val constraint = ForeignKeyConstraint(
                    name = suffix(table.name, "_fk$i"),
                    foreignColumn = foreignTableColumn,
                    column = column,
                )
                foreignKeyConstraints.add(constraint)
                table.foreignKeyConstraints.add(constraint)
            }
        }
        renderers.addAll(foreignKeyConstraints)
        return renderers
    }

}

class MaterializedViewGenerator(
    private val count: Int,
    private val tables: List<Table>,
) : Generator {
    override fun generate(): List<Renderer> {
        val chosenTables: List<Table> = tables
            .sortedBy { e -> e.foreignKeyConstraints.size }
            .filter { e -> e.foreignKeyConstraints.size > 1 }
            .take(count)
            .reversed()
        val renderers: MutableList<Renderer> = ArrayList()
        for (table in chosenTables) {
            renderers.add(
                MaterializedView(
                    name = prefix("mv_", table.name),
                    columns = table.columns.filter { it.foreignColumn != null }
                )
            )
        }
        return renderers
    }
}

class NotNullCheckConstraintGenerator(private val tables: List<Table>) : Generator {
    override fun generate(): List<Renderer> {
        val randomDataGenerator = RandomDataGenerator()
        val renderers: MutableList<Renderer> = ArrayList()
        val notNullCheckConstraints: MutableList<NotNullCheckConstraint> = ArrayList()
        for (table in tables) {
            val allExceptId: List<Column> = table.columns
                .filter { e -> e.name.lowercase() != "id" }
            val column = allExceptId[randomDataGenerator.nextInt(0, allExceptId.size - 1)]
            notNullCheckConstraints.add(
                NotNullCheckConstraint(
                    name = suffix(table.name, "_ck"),
                    column = column,
                )
            )
        }
        renderers.addAll(notNullCheckConstraints)
        return renderers
    }
}

class PrimaryKeyConstraintGenerator(private val tables: List<Table>) : Generator {
    override fun generate(): List<Renderer> {
        val renderers: MutableList<Renderer> = ArrayList()
        val primaryKeyConstraints: MutableList<PrimaryKeyConstraint> = ArrayList()
        for (table in tables) {
            val id = table.columns.firstOrNull { it.name.lowercase() == "id" } ?: continue
            primaryKeyConstraints.add(
                PrimaryKeyConstraint(
                    name = shorten(table.name, 27) + "_pk",
                    column = id,
                )
            )
        }
        renderers.addAll(primaryKeyConstraints)
        return renderers
    }
}

class TableGenerator(private val count: Int) : Generator {
    override fun generate(): List<Renderer> {
        val faker = Faker()
        val randomDataGenerator = RandomDataGenerator()
        val columnTypes: MutableList<ColumnTypeRenderer> = ArrayList()
        val varchar2TableColumnTypes: MutableList<Varchar2ColumnType> = ArrayList()
        for (i in 0..4) {
            varchar2TableColumnTypes.add(
                Varchar2ColumnType(
                    size = randomDataGenerator.nextInt(1, 4000),
                )
            )
        }
        columnTypes.addAll(varchar2TableColumnTypes)
        columnTypes.add(NumberColumnType())
        columnTypes.add(TimestampColumnType())
        columnTypes.add(ClobColumnType())
        columnTypes.add(DateColumnType())
        columnTypes.add(DecimalColumnType())
        columnTypes.add(FloatColumnType())
        columnTypes.add(IntegerColumnType())
        columnTypes.add(NumericColumnType())
        columnTypes.add(SmallIntColumnType())
        val renderers: MutableList<Renderer> = ArrayList()
        val tables: MutableList<Table> = ArrayList()
        for (i in 0 until count) {
            val table = Table(
                name = unique(tables.map { it.name }) { faker.gameOfThrones().house() },
            )
            val id = Column(
                name = "id",
                columnType = Varchar2ColumnType(size = 36),
                defaultValue = "sys_guid()",
                table = table,
            )
            table.columns.add(id)
            for (j in 0 until randomDataGenerator.nextInt(3, 100)) {
                table.columns.add(
                    Column(
                        name = unique(table.columns.map { it.name }) { faker.gameOfThrones().character() },
                        columnType = columnTypes[randomDataGenerator.nextInt(0, columnTypes.size - 1)],
                        table = table,
                    )
                )
            }
            table.columns.add(
                Column(
                    name = "create_time",
                    columnType = TimestampColumnType(),
                    defaultValue = "sysdate",
                    table = table
                )
            )
            table.columns.add(
                Column(
                    name = "update_time",
                    columnType = TimestampColumnType(),
                    defaultValue = "sysdate",
                    table = table,
                )
            )
            tables.add(table)
        }
        renderers.addAll(tables)
        return renderers
    }
}

class ViewGenerator(private val count: Int, private val tables: List<Table>) : Generator {
    override fun generate(): List<Renderer> {
        val chosenTables: List<Table> = tables
            .sortedBy { it.foreignKeyConstraints.size }
            .filter { it.foreignKeyConstraints.size > 1 }
            .takeLast(count)
            .reversed()
        val renderers: MutableList<Renderer> = ArrayList()
        for (table in chosenTables) {
            renderers.add(
                View(
                    name = prefix("v_", table.name),
                    columns = table.columns.filter { it.foreignColumn != null }
                )
            )
        }
        return renderers
    }
}

class ProcedureGenerator(val tables: List<Table>) : Generator {
    override fun generate(): List<Renderer> {
        return ArrayList()
    }
}

class FunctionGenerator : Generator {
    override fun generate(): List<Renderer> {
        return ArrayList()
    }
}

class PackageGenerator : Generator {
    override fun generate(): List<Renderer> {
        return ArrayList()
    }
}