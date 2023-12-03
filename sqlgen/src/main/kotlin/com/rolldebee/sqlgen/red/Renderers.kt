package com.rolldebee.sqlgen.red

import com.rolldebee.sqlgen.core.ColumnTypeRenderer
import com.rolldebee.sqlgen.core.RenderOptions
import com.rolldebee.sqlgen.core.Renderer
import com.rolldebee.sqlgen.core.TabularObjectRenderer

class ClobColumnType : ColumnTypeRenderer {
    override fun render(options: RenderOptions): String {
        return name()
    }

    override fun name(): String {
        return "clob"
    }
}

class DateColumnType : ColumnTypeRenderer {
    override fun render(options: RenderOptions): String {
        return name()
    }

    override fun name(): String {
        return "date"
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

class NumberColumnType : ColumnTypeRenderer {
    override fun render(options: RenderOptions): String {
        return name()
    }

    override fun name(): String {
        return "number"
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

class TimestampColumnType : ColumnTypeRenderer {
    override fun render(options: RenderOptions): String {
        return name()
    }

    override fun name(): String {
        return "timestamp"
    }
}

class Varchar2ColumnType(
    val size: Int = 0,
) : ColumnTypeRenderer {
    override fun render(options: RenderOptions): String {
        return name() + "(" + size + ")"
    }

    override fun name(): String {
        return "varchar2"
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

class Column(
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

class MaterializedView(
    val name: String,
    val columns: List<Column> = ArrayList(),
) : TabularObjectRenderer {
    override fun render(options: RenderOptions): String {
        val primaryTable = columns.stream().map(Column::table).findFirst().get()
        val buffer = StringBuilder()
        buffer.append("create materialized view ")
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

class Procedure(private val tableName: String, private val columns: List<String>) : Renderer {
    override fun render(options: RenderOptions): String {
        val buffer = StringBuilder()
        buffer.append("create or replace procedure print_$tableName(p_id varchar2) as" + options.newLine())
        buffer.append(options.indent() + "v_result $tableName%rowtype;" + options.newLine())
        buffer.append("begin" + options.newLine())
        buffer.append(options.indent() + "select * into v_result from $tableName where $tableName.id = p_id;" + options.newLine())
        buffer.append(options.indent() + "dbms_output.put_line(" + options.newLine())
        buffer.append(
            columns.joinToString(separator = " || ' ' ||" + options.newLine()) {
                options.indent().repeat(2) + "v_result.$it"
            })
        buffer.append(options.newLine() + options.indent() + ");" + options.newLine())
        buffer.append("exception" + options.newLine())
        buffer.append("when others then" + options.newLine())
        buffer.append(options.indent() + "dbms_output.put_line(SQLERRM);" + options.newLine())
        buffer.append("end;" + options.newLine())
        buffer.append("/" + options.newLine())
        return buffer.toString()
    }
}

class Function(private val tableName: String, private val columns: List<String>) : Renderer {
    override fun render(options: RenderOptions): String {
        val buffer = StringBuilder()
        buffer.append("create or replace function get_$tableName(p_id varchar2) return varchar2 is" + options.newLine())
        buffer.append(options.indent() + "v_result $tableName%rowtype;" + options.newLine())
        buffer.append("begin" + options.newLine())
        buffer.append(options.indent() + "select * into v_result from $tableName where $tableName.id = p_id;" + options.newLine())
        buffer.append(options.indent() + "dbms_output.put_line(" + options.newLine())
        buffer.append(
            columns.joinToString(separator = " || ' ' ||" + options.newLine()) {
                options.indent().repeat(2) + "v_result.$it"
            })
        buffer.append(options.newLine() + options.indent() + ");" + options.newLine())
        buffer.append(options.indent() + "return v_result.id;" + options.newLine())
        buffer.append("exception" + options.newLine())
        buffer.append("when others then" + options.newLine())
        buffer.append(options.indent() + "dbms_output.put_line(SQLERRM);" + options.newLine())
        buffer.append(options.indent() + "return null;" + options.newLine())
        buffer.append("end;" + options.newLine())
        buffer.append("/" + options.newLine())
        return buffer.toString()
    }
}

class Package(private val tableName: String, private val columns: List<String>) : Renderer {
    override fun render(options: RenderOptions): String {
        val buffer = StringBuilder()
        buffer.append("create or replace package pkg_$tableName as" + options.newLine())
        buffer.append(options.indent() + "function get_$tableName(p_id varchar2) return varchar2;" + options.newLine())
        buffer.append(options.indent() + "procedure print_$tableName(p_id varchar2);" + options.newLine())
        buffer.append("end pkg_$tableName;" + options.newLine())
        buffer.append(options.newLine())
        buffer.append("create or replace package body pkg_$tableName as" + options.newLine())
        buffer.append(renderProcedure(options))
        buffer.append(options.newLine())
        buffer.append(renderFunction(options))
        buffer.append("end pkg_$tableName;" + options.newLine())
        return buffer.toString()
    }

    private fun renderProcedure(options: RenderOptions): String {
        val buffer = StringBuilder()
        buffer.append(options.indent() + "procedure print_$tableName(p_id varchar2) as" + options.newLine())
        buffer.append(options.indent().repeat(2) + "v_result $tableName%rowtype;" + options.newLine())
        buffer.append(options.indent() + "begin" + options.newLine())
        buffer.append(
            options.indent()
                .repeat(2) + "select * into v_result from $tableName where $tableName.id = p_id;" + options.newLine()
        )
        buffer.append(options.indent().repeat(2) + "dbms_output.put_line(" + options.newLine())
        buffer.append(
            columns.joinToString(separator = " || ' ' ||" + options.newLine()) {
                options.indent().repeat(3) + "v_result.$it"
            })
        buffer.append(options.newLine() + options.indent().repeat(2) + ");" + options.newLine())
        buffer.append(options.indent() + "exception" + options.newLine())
        buffer.append(options.indent() + "when others then" + options.newLine())
        buffer.append(options.indent().repeat(2) + "dbms_output.put_line(SQLERRM);" + options.newLine())
        buffer.append(options.indent() + "end;" + options.newLine())
        return buffer.toString()
    }

    private fun renderFunction(options: RenderOptions): String {
        val buffer = StringBuilder()
        buffer.append(options.indent() + "function get_$tableName(p_id varchar2) return varchar2 is" + options.newLine())
        buffer.append(options.indent().repeat(2) + "v_result $tableName%rowtype;" + options.newLine())
        buffer.append(options.indent() + "begin" + options.newLine())
        buffer.append(
            options.indent()
                .repeat(2) + "select * into v_result from $tableName where $tableName.id = p_id;" + options.newLine()
        )
        buffer.append(options.indent().repeat(2) + "dbms_output.put_line(" + options.newLine())
        buffer.append(
            columns.joinToString(separator = " || ' ' ||" + options.newLine()) {
                options.indent().repeat(3) + "v_result.$it"
            })
        buffer.append(options.newLine() + options.indent().repeat(2) + ");" + options.newLine())
        buffer.append(options.indent().repeat(2) + "return v_result.id;" + options.newLine())
        buffer.append(options.indent() + "exception" + options.newLine())
        buffer.append(options.indent() + "when others then" + options.newLine())
        buffer.append(options.indent().repeat(2) + "dbms_output.put_line(SQLERRM);" + options.newLine())
        buffer.append(options.indent().repeat(2) + "return null;" + options.newLine())
        buffer.append(options.indent() + "end;" + options.newLine())
        return buffer.toString()
    }
}

class Sequence(private val tableName: String) : Renderer {
    override fun render(options: RenderOptions): String {
        return "CREATE SEQUENCE seq_$tableName INCREMENT BY 1 START WITH 1;"
    }
}