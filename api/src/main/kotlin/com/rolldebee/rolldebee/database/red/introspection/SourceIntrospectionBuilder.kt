package com.rolldebee.rolldebee.database.red.introspection

import com.rolldebee.rolldebee.core.Source
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class SourceIntrospectionBuilder {
    fun build(jdbcTemplate: NamedParameterJdbcTemplate): List<Source> {
        val sources = ArrayList<Source>()
        val rows = jdbcTemplate.query(
            """select name,
                       type,
                       text as ddl
                from user_source
                where type in ('FUNCTION', 'PROCEDURE', 'PACKAGE', 'PACKAGE BODY', 'TYPE', 'TYPE BODY', 'TRIGGER') and 
                    name not like 'BIN$%'
                order by name, type, line asc
            """
        ) { resultSet, _ ->
            Row(
                name = resultSet.getString("NAME"),
                type = resultSet.getString("TYPE"),
                ddl = resultSet.getString("DDL"),
            )
        }
        rows.forEach {
            val existing = sources.find { source -> source.name == it.name && source.objectType == it.type }
            if (existing == null) {
                var status = ""
                jdbcTemplate.query(
                    "select status from user_objects where object_name = :object_name", mapOf("object_name" to it.name)
                ) { resultSet, _ -> status = resultSet.getString("STATUS") }
                sources.add(
                    Source(
                        name = it.name,
                        ddl = "CREATE ${it.ddl}",
                        dropDdl = when (it.type) {
                            "TYPE" -> "DROP ${it.type} ${it.name} CASCADE"
                            else -> "DROP ${it.type} ${it.name}"
                        },
                        objectType = it.type,
                        status = status,
                    )
                )
            } else {
                existing.ddl = existing.ddl + it.ddl
            }
        }
        return sources
    }

    data class Row(var name: String, var type: String, var ddl: String)
}
