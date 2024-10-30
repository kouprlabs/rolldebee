package com.rolldebee.rolldebee.factory

import com.rolldebee.rolldebee.core.TriggerEnabler
import com.rolldebee.rolldebee.database.mysql.MySqlTriggerEnabler
import com.rolldebee.rolldebee.database.postgres.PostgresTriggerEnabler
import com.rolldebee.rolldebee.database.red.RedTriggerEnabler
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.entity.DatabaseType
import org.springframework.stereotype.Service

@Service
class TriggerEnablerFactory(
    val postgresTriggerEnabler: PostgresTriggerEnabler,
    val mySqlTriggerEnabler: MySqlTriggerEnabler,
    val redTriggerEnabler: RedTriggerEnabler,
) {
    fun get(connection: Connection): TriggerEnabler =
        when (connection.databaseType) {
            DatabaseType.POSTGRES -> {
                postgresTriggerEnabler
            }
            DatabaseType.MYSQL -> {
                mySqlTriggerEnabler
            }
            DatabaseType.RED -> {
                redTriggerEnabler
            }
            else -> {
                throw NoSuchElementException()
            }
        }
}
