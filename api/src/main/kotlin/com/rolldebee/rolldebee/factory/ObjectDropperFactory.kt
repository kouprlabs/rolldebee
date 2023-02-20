package com.rolldebee.rolldebee.factory

import com.rolldebee.rolldebee.core.ObjectDropper
import com.rolldebee.rolldebee.database.mysql.MySqlObjectDropper
import com.rolldebee.rolldebee.database.postgres.PostgresObjectDropper
import com.rolldebee.rolldebee.database.red.RedObjectDropper
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.entity.DatabaseType
import org.springframework.stereotype.Service

@Service
class ObjectDropperFactory(
    val postgresObjectDropper: PostgresObjectDropper,
    val mySqlObjectDropper: MySqlObjectDropper,
    val redObjectDropper: RedObjectDropper,
) {
    fun get(connection: Connection): ObjectDropper {
        return when (connection.databaseType) {
            DatabaseType.POSTGRES -> {
                postgresObjectDropper
            }
            DatabaseType.MYSQL -> {
                mySqlObjectDropper
            }
            DatabaseType.RED -> {
                redObjectDropper
            }
            else -> {
                throw NoSuchElementException()
            }
        }
    }
}
