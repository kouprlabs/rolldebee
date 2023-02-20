package com.rolldebee.rolldebee.database.red

import com.rolldebee.rolldebee.core.DatabaseObject
import com.rolldebee.rolldebee.core.Introspection
import com.rolldebee.rolldebee.core.ObjectGraph
import com.rolldebee.rolldebee.core.ObjectGraphBuilder
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.infra.JdbcTemplateBuilder
import org.springframework.stereotype.Service
import kotlin.collections.set

@Service
class RedObjectGraphBuilder(val jdbcTemplateBuilder: JdbcTemplateBuilder) : ObjectGraphBuilder {
    override fun build(introspection: Introspection, connection: Connection): ObjectGraph {
        val nodes = ArrayList<ObjectGraph.Node>()
        append(introspection.views, nodes)
        append(introspection.tables, nodes)
        append(introspection.sequences, nodes)
        append(introspection.sources, nodes)
        append(introspection.materializedViews, nodes)
        append(introspection.indexes, nodes)
        append(introspection.constraints, nodes)
        val jdbcTemplate = jdbcTemplateBuilder.build(connection)
        for (node in nodes) {
            val params: MutableMap<String, String> = HashMap()
            params["owner"] = connection.jdbcUsername
            params["name"] = node.databaseObject.name
            params["object_type"] = node.databaseObject.objectType
            val rows = jdbcTemplate.query(
                """select *
                    from user_dependencies
                    where lower(referenced_owner) = lower(:owner)
                      and lower(name) = lower(:name)
                      and lower(type) = lower(:object_type)
                """,
                params
            ) { resultSet, _ ->
                val dependencyRow = Row(
                    name = resultSet.getString("NAME"),
                    type = resultSet.getString("TYPE"),
                    referencedName = resultSet.getString("REFERENCED_NAME"),
                    referencedType = resultSet.getString("REFERENCED_TYPE")
                )
                dependencyRow
            }
            val dependencies = ArrayList<ObjectGraph.Node>()
            for (row in rows) {
                val found = nodes.find {
                    it.databaseObject.name == row.referencedName && it.databaseObject.objectType == row.referencedType
                }
                if (found != null) {
                    dependencies.add(found)
                }
            }
            node.dependencies = dependencies
        }
        return ObjectGraph(nodes = nodes)
    }

    private fun append(objects: List<DatabaseObject>, objectGraph: ArrayList<ObjectGraph.Node>) {
        objects.forEach { objectGraph.add(ObjectGraph.Node(databaseObject = it)) }
    }

    data class Row(
        var name: String,
        var type: String,
        var referencedName: String,
        var referencedType: String,
    )
}
