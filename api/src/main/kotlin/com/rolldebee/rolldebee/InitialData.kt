package com.rolldebee.rolldebee

import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.entity.DatabaseType
import com.rolldebee.rolldebee.repository.ConnectionRepository
import org.springframework.context.annotation.Configuration
import jakarta.annotation.PostConstruct

@Configuration
class InitialData(
    val connectionRepository: ConnectionRepository
) {

    @PostConstruct
    fun connections() {
        if (connectionRepository.count() == 0L) {
            var host = System.getenv("ORACLE_HOST")
            if (host.isNullOrBlank()) {
                host = "localhost"
            }
            connectionRepository.save(Connection(
                name = "Schema 1",
                jdbcUrl = "jdbc:oracle:thin:@$host:1521:free",
                jdbcUsername = "schema1",
                jdbcPassword = "schema1",
                databaseType = DatabaseType.RED,
            ))
            connectionRepository.save(Connection(
                name = "Schema 2",
                jdbcUrl = "jdbc:oracle:thin:@$host:1521:free",
                jdbcUsername = "schema2",
                jdbcPassword = "schema2",
                databaseType = DatabaseType.RED,
            ))
        }
    }
}
