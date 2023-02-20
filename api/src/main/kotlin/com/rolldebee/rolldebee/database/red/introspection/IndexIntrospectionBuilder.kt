package com.rolldebee.rolldebee.database.red.introspection

import com.rolldebee.rolldebee.core.Index
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class IndexIntrospectionBuilder {
    fun build(jdbcTemplate: NamedParameterJdbcTemplate): List<Index> {
        val rows = jdbcTemplate.query(
            """select index_name,
                       index_type,
                       table_name,
                       status,
                       dbms_metadata.get_ddl('INDEX', index_name) as ddl
                from user_indexes
                where lower(index_type) = 'normal' and index_name not like 'BIN$%'
            """
        ) { resultSet, _ ->
            Row(
                indexName = resultSet.getString("INDEX_NAME"),
                indexType = resultSet.getString("INDEX_TYPE"),
                status = resultSet.getString("STATUS"),
                tableName = resultSet.getString("TABLE_NAME"),
                ddl = resultSet.getString("DDL").trim(),
            )
        }
        return rows.map {
            Index(
                name = it.indexName,
                ddl = it.ddl,
                status = it.status,
                indexType = it.indexType,
                dropDdl = "DROP INDEX ${it.indexName}"
            )
        }
    }

    data class Row(
        var indexName: String,
        var indexType: String,
        var tableName: String,
        var status: String,
        var ddl: String,
    )
}
