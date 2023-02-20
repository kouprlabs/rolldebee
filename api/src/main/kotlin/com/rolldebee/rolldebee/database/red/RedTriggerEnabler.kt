package com.rolldebee.rolldebee.database.red

import com.rolldebee.rolldebee.core.EnableTriggerSummary
import com.rolldebee.rolldebee.core.TriggerEnabler
import com.rolldebee.rolldebee.database.red.introspection.SourceIntrospectionBuilder
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.infra.JdbcTemplateBuilder
import org.springframework.stereotype.Service

@Service
class RedTriggerEnabler(
    val sourceIntrospectionBuilder: SourceIntrospectionBuilder,
    val jdbcTemplateBuilder: JdbcTemplateBuilder,
) : TriggerEnabler {
    override fun run(connection: Connection): EnableTriggerSummary {
        val summary = EnableTriggerSummary()
        val jdbcTemplate = jdbcTemplateBuilder.build(connection)
        val sources = sourceIntrospectionBuilder.build(jdbcTemplate)
        for (source in sources) {
            if (source.objectType == "TRIGGER") {
                try {
                    jdbcTemplate.jdbcOperations.execute("alter trigger ${source.name} enable")
                    summary.successes.add(EnableTriggerSummary.Success(name = source.name))
                } catch (e: Exception) {
                    summary.failures.add(
                        EnableTriggerSummary.Failure(
                            name = source.name, reason = e.message.toString()
                        )
                    )
                }
            }
        }
        return summary
    }
}
