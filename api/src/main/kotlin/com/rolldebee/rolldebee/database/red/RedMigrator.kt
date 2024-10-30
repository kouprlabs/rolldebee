package com.rolldebee.rolldebee.database.red

import com.rolldebee.rolldebee.core.Comparison
import com.rolldebee.rolldebee.core.MigrationSummary
import com.rolldebee.rolldebee.core.Migrator
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.infra.JdbcTemplateBuilder
import org.springframework.stereotype.Service

@Service
class RedMigrator(
    val jdbcTemplateBuilder: JdbcTemplateBuilder,
) : Migrator {
    override fun run(
        comparison: Comparison,
        connection: Connection,
    ): MigrationSummary {
        val succeeded = ArrayList<Comparison.Diff>()
        val failure = ArrayList<MigrationSummary.Failure>()
        val jdbcTemplate = jdbcTemplateBuilder.build(connection)
        for (diff in comparison.diffs) {
            val failureDdls = ArrayList<MigrationSummary.Failure.Ddl>()
            for (ddl in diff.ddls) {
                try {
                    jdbcTemplate.jdbcOperations.execute(ddl)
                } catch (e: Exception) {
                    val reason =
                        e.cause
                            .toString()
                            .replace("java.sql.SQLSyntaxErrorException: ", "")
                            .replace("\n", "")

                    // Object does not exist
                    if (reason.contains("ORA-04043") && ddl.contains("DROP")) {
                        continue
                    }
                    // Specified index does not exist
                    if (reason.contains("ORA-01418") && ddl.contains("DROP")) {
                        continue
                    }
                    // Cannot drop constraint  - nonexistent constraint
                    if (reason.contains("ORA-02443") && ddl.contains("DROP")) {
                        continue
                    }
                    // Such column list already indexed
                    if (reason.contains("ORA-01408") && ddl.contains("CREATE")) {
                        continue
                    }
                    failureDdls.add(MigrationSummary.Failure.Ddl(ddl, reason))
                }
            }
            if (failureDdls.isEmpty()) {
                succeeded.add(diff)
            } else {
                failure.add(MigrationSummary.Failure(diff = diff, ddls = failureDdls))
            }
        }
        return MigrationSummary(failures = failure, succeeded = succeeded)
    }
}
