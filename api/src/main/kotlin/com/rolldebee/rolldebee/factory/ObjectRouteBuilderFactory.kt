package com.rolldebee.rolldebee.factory

import com.rolldebee.rolldebee.core.ObjectRouteBuilder
import com.rolldebee.rolldebee.database.mysql.MySqlObjectRouteBuilder
import com.rolldebee.rolldebee.database.postgres.PostgresObjectRouteBuilder
import com.rolldebee.rolldebee.database.red.RedObjectRouteBuilder
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.entity.DatabaseType
import org.springframework.stereotype.Service

@Service
class ObjectRouteBuilderFactory(
    val postgresObjectRouteBuilder: PostgresObjectRouteBuilder,
    val mySqlObjectRouteBuilder: MySqlObjectRouteBuilder,
    val redObjectRouteBuilder: RedObjectRouteBuilder,
) {
    fun get(connection: Connection): ObjectRouteBuilder {
        return when (connection.databaseType) {
            DatabaseType.POSTGRES -> {
                postgresObjectRouteBuilder
            }
            DatabaseType.MYSQL -> {
                mySqlObjectRouteBuilder
            }
            DatabaseType.RED -> {
                redObjectRouteBuilder
            }
            else -> {
                throw NoSuchElementException()
            }
        }
    }
}
