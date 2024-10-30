package com.rolldebee.rolldebee.factory

import com.rolldebee.rolldebee.core.MigrationScriptBuilder
import com.rolldebee.rolldebee.database.mysql.MySqlMigrationScriptBuilder
import com.rolldebee.rolldebee.database.postgres.PostgresMigrationScriptBuilder
import com.rolldebee.rolldebee.database.red.RedMigrationScriptBuilder
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.entity.DatabaseType
import org.springframework.stereotype.Service

@Service
class MigrationScriptBuilderFactory(
    val postgresMigrationScriptBuilder: PostgresMigrationScriptBuilder,
    val mySqlMigrationScriptBuilder: MySqlMigrationScriptBuilder,
    val redMigrationScriptBuilder: RedMigrationScriptBuilder,
) {
    fun get(connection: Connection): MigrationScriptBuilder =
        when (connection.databaseType) {
            DatabaseType.POSTGRES -> {
                postgresMigrationScriptBuilder
            }
            DatabaseType.MYSQL -> {
                mySqlMigrationScriptBuilder
            }
            DatabaseType.RED -> {
                redMigrationScriptBuilder
            }
            else -> {
                throw NoSuchElementException()
            }
        }
}
