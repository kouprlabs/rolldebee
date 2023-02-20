package com.rolldebee.rolldebee.factory

import com.rolldebee.rolldebee.core.Migrator
import com.rolldebee.rolldebee.database.mysql.MySqlMigrator
import com.rolldebee.rolldebee.database.postgres.PostgresMigrator
import com.rolldebee.rolldebee.database.red.RedMigrator
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.entity.DatabaseType
import org.springframework.stereotype.Service

@Service
class MigratorFactory(
    val postgresMigrator: PostgresMigrator,
    val mySqlMigrator: MySqlMigrator,
    val redMigrator: RedMigrator,
) {
    fun get(connection: Connection): Migrator {
        return when (connection.databaseType) {
            DatabaseType.POSTGRES -> {
                postgresMigrator
            }
            DatabaseType.MYSQL -> {
                mySqlMigrator
            }
            DatabaseType.RED -> {
                redMigrator
            }
            else -> {
                throw NoSuchElementException()
            }
        }
    }
}
