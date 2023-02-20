package com.rolldebee.rolldebee.factory

import com.rolldebee.rolldebee.core.ConstraintGraphBuilder
import com.rolldebee.rolldebee.database.mysql.MySqlConstraintGraphBuilder
import com.rolldebee.rolldebee.database.postgres.PostgresConstraintGraphBuilder
import com.rolldebee.rolldebee.database.red.RedConstraintGraphBuilder
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.entity.DatabaseType
import org.springframework.stereotype.Service

@Service
class ConstraintGraphBuilderFactory(
    val postgresConstraintGraphBuilder: PostgresConstraintGraphBuilder,
    val mySqlConstraintGraphBuilder: MySqlConstraintGraphBuilder,
    val redConstraintGraphBuilder: RedConstraintGraphBuilder,
) {
    fun get(connection: Connection): ConstraintGraphBuilder {
        return when (connection.databaseType) {
            DatabaseType.POSTGRES -> {
                postgresConstraintGraphBuilder
            }
            DatabaseType.MYSQL -> {
                mySqlConstraintGraphBuilder
            }
            DatabaseType.RED -> {
                redConstraintGraphBuilder
            }
            else -> {
                throw NoSuchElementException()
            }
        }
    }
}
