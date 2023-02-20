package com.rolldebee.rolldebee.factory

import com.rolldebee.rolldebee.core.IntrospectionBuilder
import com.rolldebee.rolldebee.database.mysql.MySqlIntrospectionBuilder
import com.rolldebee.rolldebee.database.postgres.PostgresIntrospectionBuilder
import com.rolldebee.rolldebee.database.red.RedIntrospectionBuilder
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.entity.DatabaseType
import org.springframework.stereotype.Service

@Service
class IntrospectionBuilderFactory(
    val postgresIntrospectionBuilder: PostgresIntrospectionBuilder,
    val mySqlIntrospectionBuilder: MySqlIntrospectionBuilder,
    val redIntrospectionBuilder: RedIntrospectionBuilder,
) {
    fun get(connection: Connection): IntrospectionBuilder {
        return when (connection.databaseType) {
            DatabaseType.POSTGRES -> {
                postgresIntrospectionBuilder
            }
            DatabaseType.MYSQL -> {
                mySqlIntrospectionBuilder
            }
            DatabaseType.RED -> {
                redIntrospectionBuilder
            }
            else -> {
                throw NoSuchElementException()
            }
        }
    }
}
