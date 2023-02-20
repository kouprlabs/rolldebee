package com.rolldebee.rolldebee.factory

import com.rolldebee.rolldebee.core.ObjectGraphBuilder
import com.rolldebee.rolldebee.database.mysql.MySqlObjectGraphBuilder
import com.rolldebee.rolldebee.database.postgres.PostgresObjectGraphBuilder
import com.rolldebee.rolldebee.database.red.RedObjectGraphBuilder
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.entity.DatabaseType
import org.springframework.stereotype.Service

@Service
class ObjectGraphBuilderFactory(
    val postgresObjectGraphBuilder: PostgresObjectGraphBuilder,
    val mySqlObjectGraphBuilder: MySqlObjectGraphBuilder,
    val redObjectGraphBuilder: RedObjectGraphBuilder,
) {
    fun get(connection: Connection): ObjectGraphBuilder {
        return when (connection.databaseType) {
            DatabaseType.POSTGRES -> {
                postgresObjectGraphBuilder
            }
            DatabaseType.MYSQL -> {
                mySqlObjectGraphBuilder
            }
            DatabaseType.RED -> {
                redObjectGraphBuilder
            }
            else -> {
                throw NoSuchElementException()
            }
        }
    }
}
