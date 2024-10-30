package com.rolldebee.sqlgen.red

import com.rolldebee.sqlgen.core.ComposeOptions
import com.rolldebee.sqlgen.core.Composer
import com.rolldebee.sqlgen.core.RenderOptions

class RedComposer : Composer {
    override fun compose(options: ComposeOptions): String {
        val renderOptions =
            RenderOptions(
                indentSize = options.indentSize,
                continuationIndentSize = options.continuationIndentSize,
            )

        val sql = StringBuilder()

        val tables = TableGenerator(options.tableCount).generate()
        val primaryKeyConstraints = PrimaryKeyConstraintGenerator(tables as List<Table>).generate()
        val nullCheckConstraints = NotNullCheckConstraintGenerator(tables).generate()
        val foreignKeyConstraints = ForeignKeyConstraintGenerator(tables, options.foreignKeyMaxCount).generate()
        val views = ViewGenerator(options.viewCount, tables).generate()
        val materializedViews = MaterializedViewGenerator(options.materializedViewCount, tables).generate()

        tables.forEachIndexed { index, table ->
            sql.append(table.render(renderOptions))
            if (index != tables.size - 1) {
                sql.append(renderOptions.newLine().repeat(2))
            }
        }
        if (tables.isNotEmpty()) {
            sql.append(renderOptions.newLine().repeat(2))
        }

        primaryKeyConstraints.forEachIndexed { index, primaryKeyConstraint ->
            sql.append(primaryKeyConstraint.render(renderOptions))
            if (index != primaryKeyConstraints.size - 1) {
                sql.append(renderOptions.newLine().repeat(2))
            }
        }
        if (primaryKeyConstraints.isNotEmpty()) {
            sql.append(renderOptions.newLine().repeat(2))
        }

        nullCheckConstraints.forEachIndexed { index, nullCheckConstraint ->
            sql.append(nullCheckConstraint.render(renderOptions))
            if (index != nullCheckConstraints.size - 1) {
                sql.append(renderOptions.newLine().repeat(2))
            }
        }
        if (nullCheckConstraints.isNotEmpty()) {
            sql.append(renderOptions.newLine().repeat(2))
        }

        foreignKeyConstraints.forEachIndexed { index, foreignKeyConstraint ->
            sql.append(foreignKeyConstraint.render(renderOptions))
            if (index != foreignKeyConstraints.size - 1) {
                sql.append(renderOptions.newLine().repeat(2))
            }
        }
        if (foreignKeyConstraints.isNotEmpty()) {
            sql.append(renderOptions.newLine().repeat(2))
        }

        views.forEachIndexed { index, view ->
            sql.append(view.render(renderOptions))
            if (index != views.size - 1) {
                sql.append(renderOptions.newLine().repeat(2))
            }
        }
        if (views.isNotEmpty()) {
            sql.append(renderOptions.newLine().repeat(2))
        }

        materializedViews.forEachIndexed { index, materializedView ->
            sql.append(materializedView.render(renderOptions))
            if (index != materializedViews.size - 1) {
                sql.append(renderOptions.newLine().repeat(2))
            }
        }
        if (materializedViews.isNotEmpty()) {
            sql.append(renderOptions.newLine().repeat(2))
        }

        tables.forEachIndexed { index, table ->
            sql.append(Procedure(table.name, table.columns.map { column -> column.name }).render(renderOptions))
            if (index != tables.size - 1) {
                sql.append(renderOptions.newLine())
            }
        }
        if (tables.isNotEmpty()) {
            sql.append(renderOptions.newLine())
        }

        tables.forEachIndexed { index, table ->
            sql.append(Function(table.name, table.columns.map { column -> column.name }).render(renderOptions))
            if (index != tables.size - 1) {
                sql.append(renderOptions.newLine())
            }
        }
        if (tables.isNotEmpty()) {
            sql.append(renderOptions.newLine())
        }

        tables.forEachIndexed { index, table ->
            sql.append(Package(table.name, table.columns.map { column -> column.name }).render(renderOptions))
            if (index != tables.size - 1) {
                sql.append(renderOptions.newLine())
            }
        }
        if (tables.isNotEmpty()) {
            sql.append(renderOptions.newLine())
        }

        tables.forEachIndexed { index, table ->
            sql.append(Sequence(table.name).render(renderOptions))
            if (index != tables.size - 1) {
                sql.append(renderOptions.newLine())
            }
        }

        sql.append(renderOptions.newLine())

        return sql.toString()
    }
}
