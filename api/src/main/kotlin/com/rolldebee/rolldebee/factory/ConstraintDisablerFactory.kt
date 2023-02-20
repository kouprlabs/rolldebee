package com.rolldebee.rolldebee.factory

import com.rolldebee.rolldebee.core.ConstraintDisabler
import com.rolldebee.rolldebee.database.mysql.MySqlConstraintDisabler
import com.rolldebee.rolldebee.database.postgres.PostgresConstraintDisabler
import com.rolldebee.rolldebee.database.red.RedConstraintDisabler
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.entity.DatabaseType
import org.springframework.stereotype.Service

@Service
class ConstraintDisablerFactory(
    val postgresConstraintDisabler: PostgresConstraintDisabler,
    val mySqlConstraintDisabler: MySqlConstraintDisabler,
    val redConstraintDisabler: RedConstraintDisabler,
) {
    fun get(connection: Connection): ConstraintDisabler {
        return when (connection.databaseType) {
            DatabaseType.POSTGRES -> {
                postgresConstraintDisabler
            }
            DatabaseType.MYSQL -> {
                mySqlConstraintDisabler
            }
            DatabaseType.RED -> {
                redConstraintDisabler
            }
            else -> {
                throw NoSuchElementException()
            }
        }
    }
}
