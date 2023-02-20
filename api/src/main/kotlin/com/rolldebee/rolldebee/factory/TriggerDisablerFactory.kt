package com.rolldebee.rolldebee.factory

import com.rolldebee.rolldebee.core.TriggerDisabler
import com.rolldebee.rolldebee.database.mysql.MySqlTriggerDisabler
import com.rolldebee.rolldebee.database.postgres.PostgresTriggerDisabler
import com.rolldebee.rolldebee.database.red.RedTriggerDisabler
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.entity.DatabaseType
import org.springframework.stereotype.Service

@Service
class TriggerDisablerFactory(
    val postgresTriggerDisabler: PostgresTriggerDisabler,
    val mySqlTriggerDisabler: MySqlTriggerDisabler,
    val redTriggerDisabler: RedTriggerDisabler,
) {
    fun get(connection: Connection): TriggerDisabler {
        return when (connection.databaseType) {
            DatabaseType.POSTGRES -> {
                postgresTriggerDisabler
            }
            DatabaseType.MYSQL -> {
                mySqlTriggerDisabler
            }
            DatabaseType.RED -> {
                redTriggerDisabler
            }
            else -> {
                throw NoSuchElementException()
            }
        }
    }
}
