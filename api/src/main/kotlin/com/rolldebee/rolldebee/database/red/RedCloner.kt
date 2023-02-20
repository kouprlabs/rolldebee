package com.rolldebee.rolldebee.database.red

import com.rolldebee.rolldebee.core.Cloner
import com.rolldebee.rolldebee.core.CloningSummary
import com.rolldebee.rolldebee.core.DatabaseObject
import com.rolldebee.rolldebee.core.ObjectRoute
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.infra.JdbcTemplateBuilder
import org.springframework.stereotype.Service

@Service
class RedCloner(val jdbcTemplateBuilder: JdbcTemplateBuilder) : Cloner {
    override fun run(objectRoute: ObjectRoute, connection: Connection): CloningSummary {
        val ignoredOraExceptions = listOf(
            "ORA-01408", // Such column list already indexed
            "ORA-02261", // Such unique or primary key already exists in the table
            "ORA-00955", // Name is already used by an existing object
        )
        val succeeded = ArrayList<DatabaseObject>()
        val failed = ArrayList<CloningSummary.Failure>()
        val jdbcTemplate = jdbcTemplateBuilder.build(connection)
        for (databaseObject in objectRoute.databaseObjects) {
            try {
                jdbcTemplate.jdbcOperations.execute(databaseObject.ddl)
                succeeded.add(databaseObject)
            } catch (e: Exception) {
                val reason = e.cause.toString().replace("java.sql.SQLSyntaxErrorException: ", "").replace("\n", "")
                if (ignoredOraExceptions.none { ignoredOra -> reason.contains(ignoredOra) }) {
                    failed.add(CloningSummary.Failure(databaseObject = databaseObject, reason = reason))
                }
            }
        }
        return CloningSummary(failures = failed, successes = succeeded)
    }
}
