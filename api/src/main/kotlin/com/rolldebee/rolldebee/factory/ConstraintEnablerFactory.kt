package com.rolldebee.rolldebee.factory

import com.rolldebee.rolldebee.core.ConstraintEnabler
import com.rolldebee.rolldebee.database.mysql.MySqlConstraintEnabler
import com.rolldebee.rolldebee.database.postgres.PostgresConstraintEnabler
import com.rolldebee.rolldebee.database.red.RedConstraintEnabler
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.entity.DatabaseType
import org.springframework.stereotype.Service

@Service
class ConstraintEnablerFactory(
    val postgresConstraintEnabler: PostgresConstraintEnabler,
    val mySqlConstraintEnabler: MySqlConstraintEnabler,
    val redConstraintEnabler: RedConstraintEnabler,
) {
    fun get(connection: Connection): ConstraintEnabler =
        when (connection.databaseType) {
            DatabaseType.POSTGRES -> {
                postgresConstraintEnabler
            }
            DatabaseType.MYSQL -> {
                mySqlConstraintEnabler
            }
            DatabaseType.RED -> {
                redConstraintEnabler
            }
            else -> {
                throw NoSuchElementException()
            }
        }
}
