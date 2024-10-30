package com.rolldebee.rolldebee.database.red

import com.rolldebee.rolldebee.core.DisableTriggerSummary
import com.rolldebee.rolldebee.core.TriggerDisabler
import com.rolldebee.rolldebee.database.red.introspection.SourceIntrospectionBuilder
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.infra.JdbcTemplateBuilder
import org.springframework.stereotype.Service

@Service
class RedTriggerDisabler(
    val sourceIntrospectionBuilder: SourceIntrospectionBuilder,
    val jdbcTemplateBuilder: JdbcTemplateBuilder,
) : TriggerDisabler {
    override fun run(connection: Connection): DisableTriggerSummary {
        val summary = DisableTriggerSummary()
        val jdbcTemplate = jdbcTemplateBuilder.build(connection)
        val sources = sourceIntrospectionBuilder.build(jdbcTemplate)
        for (source in sources) {
            if (source.objectType == "TRIGGER") {
                try {
                    jdbcTemplate.jdbcOperations.execute("alter trigger ${source.name} disable")
                    summary.successes.add(DisableTriggerSummary.Success(name = source.name))
                } catch (e: Exception) {
                    summary.failures.add(
                        DisableTriggerSummary.Failure(
                            name = source.name,
                            reason = e.message.toString(),
                        ),
                    )
                }
            }
        }
        return summary
    }
}
