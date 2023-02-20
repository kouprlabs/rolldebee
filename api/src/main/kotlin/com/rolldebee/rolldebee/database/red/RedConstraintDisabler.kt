package com.rolldebee.rolldebee.database.red

import com.rolldebee.rolldebee.core.DisableConstraintSummary
import com.rolldebee.rolldebee.core.ConstraintDisabler
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.infra.JdbcTemplateBuilder
import org.springframework.stereotype.Service

@Service
class RedConstraintDisabler(
    val constraintGraphBuilder: RedConstraintGraphBuilder,
    val constraintRouteBuilder: RedConstraintRouteBuilder,
    val jdbcTemplateBuilder: JdbcTemplateBuilder,
) : ConstraintDisabler {
    override fun run(connection: Connection): DisableConstraintSummary {
        val summary = DisableConstraintSummary()
        val jdbcTemplate = jdbcTemplateBuilder.build(connection)
        val constraintGraph = constraintGraphBuilder.build(jdbcTemplate)
        val constraintRoute = constraintRouteBuilder.build(constraintGraph)
        for (constraint in constraintRoute.constraints.reversed()) {
            try {
                jdbcTemplate.jdbcOperations.execute("alter table ${constraint.tableName} disable constraint ${constraint.name}")
                summary.successes.add(
                    DisableConstraintSummary.Success(
                        tableName = constraint.tableName, constraintName = constraint.name
                    )
                )
            } catch (e: Exception) {
                summary.failures.add(
                    DisableConstraintSummary.Failure(
                        tableName = constraint.tableName,
                        constraintName = constraint.name,
                        reason = e.message.toString()
                    )
                )
            }
        }
        return summary
    }
}
