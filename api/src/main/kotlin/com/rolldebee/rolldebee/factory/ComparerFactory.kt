package com.rolldebee.rolldebee.factory

import com.rolldebee.rolldebee.core.ComparisonBuilder
import com.rolldebee.rolldebee.database.mysql.MySqlComparisonBuilder
import com.rolldebee.rolldebee.database.postgres.PostgresComparisonBuilder
import com.rolldebee.rolldebee.database.red.RedComparisonBuilder
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.entity.DatabaseType
import org.springframework.stereotype.Service

@Service
class ComparerFactory(
    val postgresComparisonBuilder: PostgresComparisonBuilder,
    val mySqlComparisonBuilder: MySqlComparisonBuilder,
    val redComparisonBuilder: RedComparisonBuilder,
) {
    fun get(connection: Connection): ComparisonBuilder {
        return when (connection.databaseType) {
            DatabaseType.POSTGRES -> {
                postgresComparisonBuilder
            }
            DatabaseType.MYSQL -> {
                mySqlComparisonBuilder
            }
            DatabaseType.RED -> {
                redComparisonBuilder
            }
            else -> {
                throw NoSuchElementException()
            }
        }
    }
}
