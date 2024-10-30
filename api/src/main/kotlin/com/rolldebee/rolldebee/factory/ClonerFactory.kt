package com.rolldebee.rolldebee.factory

import com.rolldebee.rolldebee.core.Cloner
import com.rolldebee.rolldebee.database.mysql.MySqlCloner
import com.rolldebee.rolldebee.database.postgres.PostgresCloner
import com.rolldebee.rolldebee.database.red.RedCloner
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.entity.DatabaseType
import org.springframework.stereotype.Service

@Service
class ClonerFactory(
    val postgresCloner: PostgresCloner,
    val mySqlCloner: MySqlCloner,
    val redCloner: RedCloner,
) {
    fun get(connection: Connection): Cloner =
        when (connection.databaseType) {
            DatabaseType.POSTGRES -> {
                postgresCloner
            }
            DatabaseType.MYSQL -> {
                mySqlCloner
            }
            DatabaseType.RED -> {
                redCloner
            }
            else -> {
                throw NoSuchElementException()
            }
        }
}
