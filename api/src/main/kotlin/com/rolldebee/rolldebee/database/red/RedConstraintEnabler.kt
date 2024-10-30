package com.rolldebee.rolldebee.database.red

import com.rolldebee.rolldebee.core.ConstraintEnabler
import com.rolldebee.rolldebee.core.EnableConstraintsSummary
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.infra.JdbcTemplateBuilder
import org.springframework.stereotype.Service

@Service
class RedConstraintEnabler(
    val constraintGraphBuilder: RedConstraintGraphBuilder,
    val constraintRouteBuilder: RedConstraintRouteBuilder,
    val jdbcTemplateBuilder: JdbcTemplateBuilder,
) : ConstraintEnabler {
    override fun run(connection: Connection): EnableConstraintsSummary {
        val summary = EnableConstraintsSummary()
        val jdbcTemplate = jdbcTemplateBuilder.build(connection)
        val constraintGraph = constraintGraphBuilder.build(jdbcTemplate)
        val constraintRoute = constraintRouteBuilder.build(constraintGraph)
        for (constraint in constraintRoute.constraints) {
            try {
                jdbcTemplate.jdbcOperations.execute("alter table ${constraint.tableName} enable novalidate constraint ${constraint.name}")
                summary.successes.add(
                    EnableConstraintsSummary.Success(
                        tableName = constraint.tableName,
                        constraintName = constraint.name,
                    ),
                )
            } catch (e: Exception) {
                summary.failures.add(
                    EnableConstraintsSummary.Failure(
                        tableName = constraint.tableName,
                        constraintName = constraint.name,
                        reason = e.message.toString(),
                    ),
                )
            }
        }
        return summary
    }
}
