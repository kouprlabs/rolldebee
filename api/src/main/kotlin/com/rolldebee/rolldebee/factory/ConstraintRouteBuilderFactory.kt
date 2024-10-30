package com.rolldebee.rolldebee.factory

import com.rolldebee.rolldebee.core.ConstraintRouteBuilder
import com.rolldebee.rolldebee.database.mysql.MySqlConstraintRouteBuilder
import com.rolldebee.rolldebee.database.postgres.PostgresConstraintRouteBuilder
import com.rolldebee.rolldebee.database.red.RedConstraintRouteBuilder
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.entity.DatabaseType
import org.springframework.stereotype.Service

@Service
class ConstraintRouteBuilderFactory(
    val postgresConstraintRouteBuilder: PostgresConstraintRouteBuilder,
    val mySqlConstraintRouteBuilder: MySqlConstraintRouteBuilder,
    val redConstraintRouteBuilder: RedConstraintRouteBuilder,
) {
    fun get(connection: Connection): ConstraintRouteBuilder =
        when (connection.databaseType) {
            DatabaseType.POSTGRES -> {
                postgresConstraintRouteBuilder
            }
            DatabaseType.MYSQL -> {
                mySqlConstraintRouteBuilder
            }
            DatabaseType.RED -> {
                redConstraintRouteBuilder
            }
            else -> {
                throw NoSuchElementException()
            }
        }
}
