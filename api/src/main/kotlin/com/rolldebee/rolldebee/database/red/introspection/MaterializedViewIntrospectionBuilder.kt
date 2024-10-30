package com.rolldebee.rolldebee.database.red.introspection

import com.rolldebee.rolldebee.core.MaterializedView
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class MaterializedViewIntrospectionBuilder {
    fun build(jdbcTemplate: NamedParameterJdbcTemplate): List<MaterializedView> {
        val rows =
            jdbcTemplate.query(
                """select mview_name, dbms_metadata.get_ddl('MATERIALIZED_VIEW', mview_name) as ddl 
                from user_mviews
                where mview_name not like 'BIN$%'
            """,
            ) { resultSet, _ ->
                Row(
                    name = resultSet.getString("MVIEW_NAME"),
                    ddl = resultSet.getString("DDL").trim(),
                )
            }
        return rows.map {
            MaterializedView(
                name = it.name,
                ddl = it.ddl,
                dropDdl = "DROP MATERIALIZED VIEW ${it.name}",
            )
        }
    }

    data class Row(
        var name: String,
        var ddl: String,
    )
}
