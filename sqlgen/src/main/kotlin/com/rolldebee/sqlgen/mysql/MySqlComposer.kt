package com.rolldebee.sqlgen.mysql

import com.rolldebee.sqlgen.core.ComposeOptions
import com.rolldebee.sqlgen.core.Composer
import com.rolldebee.sqlgen.core.RenderOptions

class MySqlComposer : Composer {
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

        tables.forEachIndexed { index, element ->
            sql.append(element.render(renderOptions))
            if (index != tables.size - 1) {
                sql.append(renderOptions.newLine().repeat(2))
            }
        }
        if (tables.isNotEmpty()) {
            sql.append(renderOptions.newLine().repeat(2))
        }

        primaryKeyConstraints.forEachIndexed { index, element ->
            sql.append(element.render(renderOptions))
            if (index != primaryKeyConstraints.size - 1) {
                sql.append(renderOptions.newLine().repeat(2))
            }
        }
        if (primaryKeyConstraints.isNotEmpty()) {
            sql.append(renderOptions.newLine().repeat(2))
        }

        nullCheckConstraints.forEachIndexed { index, element ->
            sql.append(element.render(renderOptions))
            if (index != nullCheckConstraints.size - 1) {
                sql.append(renderOptions.newLine().repeat(2))
            }
        }
        if (nullCheckConstraints.isNotEmpty()) {
            sql.append(renderOptions.newLine().repeat(2))
        }

        foreignKeyConstraints.forEachIndexed { index, element ->
            sql.append(element.render(renderOptions))
            if (index != foreignKeyConstraints.size - 1) {
                sql.append(renderOptions.newLine().repeat(2))
            }
        }
        if (foreignKeyConstraints.isNotEmpty()) {
            sql.append(renderOptions.newLine().repeat(2))
        }

        views.forEachIndexed { index, element ->
            sql.append(element.render(renderOptions))
            if (index != views.size - 1) {
                sql.append(renderOptions.newLine().repeat(2))
            }
        }

        sql.append(renderOptions.newLine())

        return sql.toString()
    }
}
