package com.rolldebee.rolldebee.database.red

import com.rolldebee.rolldebee.core.ConstraintGraph
import com.rolldebee.rolldebee.core.ConstraintGraphBuilder
import com.rolldebee.rolldebee.database.red.introspection.ConstrainIntrospectionBuilder
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class RedConstraintGraphBuilder(
    val constraintIntrospectionService: ConstrainIntrospectionBuilder,
) : ConstraintGraphBuilder {
    override fun build(jdbcTemplate: NamedParameterJdbcTemplate): ConstraintGraph {
        val nodes = ArrayList<ConstraintGraph.Node>()
        val constraints = constraintIntrospectionService.build(jdbcTemplate)
        for (constraint in constraints) {
            val dependencies = ArrayList<ConstraintGraph.Node>()
            dependencies(constraint.name, jdbcTemplate).forEach { name ->
                val dependency = constraints.find { it.name == name }
                if (dependency != null) {
                    dependencies.add(ConstraintGraph.Node(dependency))
                }
            }
            nodes.add(ConstraintGraph.Node(constraint, dependencies))
        }
        return ConstraintGraph(nodes = nodes)
    }

    private fun dependencies(
        name: String,
        jdbcTemplate: NamedParameterJdbcTemplate,
    ): List<String> {
        val result = ArrayList<String>()
        val rows =
            jdbcTemplate.query(
                """select r_constraint_name from user_constraints
                where constraint_name not like 'BIN$%'
                  and constraint_type = 'R'
                  and constraint_name = :constraint_name
            """,
                mapOf("constraint_name" to name),
            ) { resultSet, _ ->
                resultSet.getString("R_CONSTRAINT_NAME")
            }
        rows.forEach {
            result.add(it)
        }
        return result
    }
}
