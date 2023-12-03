package com.rolldebee.sqlgen.mysql

import com.rolldebee.sqlgen.core.ColumnTypeRenderer
import com.rolldebee.sqlgen.core.RenderOptions
import com.rolldebee.sqlgen.core.Renderer
import com.rolldebee.sqlgen.core.TabularObjectRenderer

class DatetimeColumnType : ColumnTypeRenderer {
    override fun render(options: RenderOptions): String {
        return name()
    }

    override fun name(): String {
        return "datetime"
    }
}

class DecimalColumnType : ColumnTypeRenderer {
    override fun render(options: RenderOptions): String {
        return name()
    }

    override fun name(): String {
        return "decimal"
    }
}

class FloatColumnType : ColumnTypeRenderer {
    override fun render(options: RenderOptions): String {
        return name()
    }

    override fun name(): String {
        return "float"
    }
}

class IntegerColumnType : ColumnTypeRenderer {
    override fun render(options: RenderOptions): String {
        return name()
    }

    override fun name(): String {
        return "integer"
    }
}

class NumericColumnType : ColumnTypeRenderer {
    override fun render(options: RenderOptions): String {
        return name()
    }

    override fun name(): String {
        return "numeric"
    }
}

class SmallIntColumnType : ColumnTypeRenderer {
    override fun render(options: RenderOptions): String {
        return name()
    }

    override fun name(): String {
        return "smallint"
    }
}

class TextColumnType : ColumnTypeRenderer {
    override fun render(options: RenderOptions): String {
        return name()
    }

    override fun name(): String {
        return "text"
    }
}

class TimestampColumnType : ColumnTypeRenderer {
    override fun render(options: RenderOptions): String {
        return name()
    }

    override fun name(): String {
        return "timestamp"
    }
}

class VarcharColumnType(
    val size: Int = 0,
) : ColumnTypeRenderer {
    override fun render(options: RenderOptions): String {
        return name() + "(" + size + ")"
    }

    override fun name(): String {
        return "varchar"
    }
}

class ForeignKeyConstraint(
    val name: String,
    val column: Column,
    val foreignColumn: Column,
) : Renderer {
    override fun render(options: RenderOptions): String {
        return "alter table " + column.table.name + options.newLine() +
                options.indent() + "add constraint " + name + " foreign key (" + column.name + ") references " +
                foreignColumn.table.name + " (" + foreignColumn.name + ");"
    }

    override fun toString(): String {
        return name
    }
}

class NotNullCheckConstraint(
    val name: String,
    val column: Column,
) : Renderer {
    override fun render(options: RenderOptions): String {
        return "alter table " + column.table.name + options.newLine() +
                options.indent() + "add constraint " + name + " check (" + column.name + " is not null);"
    }

    override fun toString(): String {
        return name
    }
}

class PrimaryKeyConstraint(
    val name: String,
    val column: Column,
) : Renderer {
    override fun render(options: RenderOptions): String {
        return "alter table " + column.table.name + options.newLine() +
                options.indent() + "add constraint " + name + " primary key (" + column.name + ");"
    }

    override fun toString(): String {
        return name
    }
}

data class Column(
    val name: String,
    val columnType: ColumnTypeRenderer,
    val notNull: Boolean = false,
    val table: Table,
    val defaultValue: String? = null,
    val foreignColumn: Column? = null,
) : Renderer {
    override fun render(options: RenderOptions): String {
        var value = name + " " + columnType.render(options)
        if (notNull) {
            value += " not null"
        }
        if (defaultValue != null && defaultValue.isNotEmpty()) {
            value += " default $defaultValue"
        }
        return value
    }

    override fun toString(): String {
        return name
    }
}

class View(
    val name: String,
    val columns: List<Column> = ArrayList(),
) : TabularObjectRenderer {
    override fun render(options: RenderOptions): String {
        val primaryTable = columns.map(Column::table).first()
        val buffer = StringBuilder()
        buffer.append("create or replace view ")
            .append(name)
            .append(" as").append(options.newLine()).append("select ")
        for ((index, column) in columns.withIndex()) {
            when (index) {
                0 -> {
                    buffer.append(column.table.name).append(".").append(column.name).append(",")
                        .append(options.newLine())
                }
                columns.size - 1 -> {
                    buffer.append(" ".repeat("select".length + 1)).append(column.table.name)
                        .append(".").append(column.name).append(options.newLine())
                }
                else -> {
                    buffer.append(" ".repeat("select".length + 1)).append(column.table.name)
                        .append(".").append(column.name).append(",").append(options.newLine())
                }
            }
        }
        buffer.append("from ").append(primaryTable.name).append(options.newLine())
        for ((index, column) in columns.withIndex()) {
            if (column.foreignColumn!!.table == primaryTable) {
                continue
            }
            buffer.append(options.continuationIndent()).append(" left join ")
                .append(column.foreignColumn.table.name)
                .append(" on ")
                .append(column.table.name).append(".").append(column.name)
                .append(" = ")
                .append(column.foreignColumn.table.name).append(".").append(column.foreignColumn.name)
            if (index != columns.size - 1) {
                buffer.append(options.newLine())
            }
        }
        buffer.append(";")
        return buffer.toString()
    }

    override fun name(): String {
        return name
    }

    override fun toString(): String {
        return name
    }
}

class Table(
    val name: String,
    val columns: ArrayList<Column> = ArrayList(),
    val foreignKeyConstraints: ArrayList<ForeignKeyConstraint> = ArrayList(),
) : TabularObjectRenderer {
    override fun render(options: RenderOptions): String {
        val buffer = StringBuilder()
        buffer.append("create table ").append(name).append(options.newLine())
        buffer.append("(").append(options.newLine())
        for ((index, column) in columns.withIndex()) {
            if (index == columns.size - 1) {
                buffer.append(options.indent()).append(column.render(options)).append(options.newLine())
            } else {
                buffer.append(options.indent()).append(column.render(options)).append(",").append(options.newLine())
            }
        }
        buffer.append(");")
        return buffer.toString()
    }

    override fun name(): String {
        return name
    }

    override fun toString(): String {
        return name
    }
}