package com.rolldebee.rolldebee.database.red

import com.rolldebee.rolldebee.core.DatabaseObject
import com.rolldebee.rolldebee.core.ObjectDropSummary
import com.rolldebee.rolldebee.core.ObjectDropper
import com.rolldebee.rolldebee.core.ObjectRoute
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.infra.JdbcTemplateBuilder
import org.springframework.stereotype.Service

@Service
class RedObjectDropper(val jdbcTemplateBuilder: JdbcTemplateBuilder) : ObjectDropper {
    override fun run(objectRoute: ObjectRoute, connection: Connection): ObjectDropSummary {
        val succeeded = ArrayList<DatabaseObject>()
        val failed = ArrayList<ObjectDropSummary.Failure>()
        val jdbcTemplate = jdbcTemplateBuilder.build(connection)
        for (databaseObject in objectRoute.databaseObjects) {
            try {
                jdbcTemplate.jdbcOperations.execute(databaseObject.dropDdl)
                succeeded.add(databaseObject)
            } catch (e: Exception) {
                val reason = e.cause.toString().replace("java.sql.SQLSyntaxErrorException: ", "").replace("\n", "")
                failed.add(ObjectDropSummary.Failure(databaseObject = databaseObject, reason = reason))
            }
        }
        return ObjectDropSummary(failures = failed, successes = succeeded)
    }
}