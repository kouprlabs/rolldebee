package com.rolldebee.rolldebee.infra

import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.entity.DatabaseType
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class JdbcTemplateBuilder {
    fun build(connection: Connection): NamedParameterJdbcTemplate {
        val dataSourceBuilder = DataSourceBuilder.create()
        when (connection.databaseType) {
            DatabaseType.RED -> dataSourceBuilder.driverClassName("oracle.jdbc.driver.OracleDriver")
            DatabaseType.POSTGRES -> dataSourceBuilder.driverClassName("org.postgresql.Driver")
            DatabaseType.MYSQL -> dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver")
            else -> throw RuntimeException("Unsupported database")
        }
        dataSourceBuilder.url(connection.jdbcUrl)
        dataSourceBuilder.username(connection.jdbcUsername)
        dataSourceBuilder.password(connection.jdbcPassword)
        return NamedParameterJdbcTemplate(dataSourceBuilder.build())
    }
}
