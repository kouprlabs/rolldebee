package com.rolldebee.rolldebee.database.red.introspection

import com.rolldebee.rolldebee.core.Column
import com.rolldebee.rolldebee.core.Table
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class TableIntrospectionBuilder {
    fun build(jdbcTemplate: NamedParameterJdbcTemplate): List<Table> {
        val tables = ArrayList<Table>()
        val rows: List<TableRow> =
            jdbcTemplate.query(
                """select ut.table_name,
                       utc.comments,
                       dbms_metadata.get_ddl('TABLE', ut.table_name) as ddl
                from user_tables ut
                       inner join user_tab_comments utc on ut.table_name = utc.table_name
                where ut.table_name not like 'BIN$%'
            """,
            ) { resultSet, _ ->
                TableRow(
                    name = resultSet.getString("TABLE_NAME"),
                    comments = resultSet.getString("COMMENTS"),
                    ddl = resultSet.getString("DDL").trim(),
                )
            }
        rows.forEach {
            tables.add(
                Table(
                    name = it.name,
                    comments = it.comments,
                    ddl = it.ddl,
                    dropDdl = "DROP TABLE ${it.name} CASCADE CONSTRAINTS",
                ),
            )
        }
        val columns = introspectColumnRows(jdbcTemplate)
        addColumnsToTables(columns, tables)
        return tables
    }

    private fun introspectColumnRows(jdbcTemplate: NamedParameterJdbcTemplate): List<ColumnRow> =
        jdbcTemplate.query(
            """select utc.table_name,
                       utc.column_name,
                       utc.data_type,
                       utc.data_length,
                       utc.data_precision,
                       utc.data_scale,
                       utc.nullable,
                       utc.column_id,
                       ucc.comments
                from user_tab_columns utc
                       inner join user_tables ut on utc.table_name = ut.table_name
                       left join user_col_comments ucc
                                 on utc.table_name = ucc.table_name and utc.column_name = ucc.column_name
                order by table_name desc
            """,
        ) { resultSet, _ ->
            ColumnRow(
                tableName = resultSet.getString("table_name"),
                columnName = resultSet.getString("column_name"),
                dataLength = resultSet.getLong("data_length"),
                dataPrecision = resultSet.getLong("data_precision"),
                dataScale = resultSet.getLong("data_scale"),
                dataType = resultSet.getString("data_type"),
                nullable = resultSet.getString("nullable") == "Y",
                columnId = resultSet.getLong("column_id"),
                comments = resultSet.getString("comments"),
            )
        }

    private fun addColumnsToTables(
        columns: List<ColumnRow>,
        tables: List<Table>,
    ) {
        columns.forEach {
            val table = tables.find { table -> table.name == it.tableName }
            if (table != null) {
                val list = ArrayList(table.columns)
                list.add(
                    Column(
                        name = it.columnName,
                        tableName = it.tableName,
                        order = it.columnId,
                        columnType = it.dataType,
                        dataLength = it.dataLength,
                        dataPrecision = it.dataPrecision,
                        dataScale = it.dataScale,
                        nullable = it.nullable,
                        comments = it.comments,
                        ddl = columnDdl(table.ddl, it.columnName),
                    ),
                )
                table.columns = list
            }
        }
    }

    private fun columnDdl(
        tableDdl: String,
        columnName: String,
    ): String =
        tableDdl
            .substring(tableDdl.indexOfFirst { it == '(' } + 1, tableDdl.indexOfLast { it == ')' })
            .split("\n")
            .map { it.trim().trimEnd(',') }
            .filter { it.isNotEmpty() }
            .find { it.contains(columnName) }!!

    data class ColumnRow(
        var tableName: String,
        var columnName: String,
        var comments: String?,
        var dataType: String,
        var dataLength: Long,
        var dataPrecision: Long,
        var dataScale: Long,
        var nullable: Boolean,
        var columnId: Long,
    )

    data class TableRow(
        var name: String,
        var comments: String?,
        var ddl: String,
    )
}
