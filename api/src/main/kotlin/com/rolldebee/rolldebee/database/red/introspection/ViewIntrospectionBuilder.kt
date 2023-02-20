package com.rolldebee.rolldebee.database.red.introspection

import com.rolldebee.rolldebee.core.View
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class ViewIntrospectionBuilder {
    fun build(jdbcTemplate: NamedParameterJdbcTemplate): List<View> {
        val views = ArrayList<View>()
        val rows = jdbcTemplate.query(
            """select view_name, dbms_metadata.get_ddl('VIEW', view_name) as ddl
                from user_views
                where view_name not like 'BIN$%'
            """
        ) { resultSet, _ ->
            ViewRow(
                name = resultSet.getString("VIEW_NAME"),
                ddl = resultSet.getString("DDL").trim(),
            )
        }
        rows.forEach {
            views.add(
                View(
                    name = it.name, ddl = it.ddl, dropDdl = "DROP VIEW ${it.name}"
                )
            )
        }
        return views
    }

    data class ViewRow(var name: String, var ddl: String)
}
