package com.rolldebee.rolldebee.database.postgres

import com.rolldebee.rolldebee.core.Comparison
import com.rolldebee.rolldebee.core.MigrationSummary
import com.rolldebee.rolldebee.core.Migrator
import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.infra.JdbcTemplateBuilder
import org.springframework.stereotype.Service

@Service
class PostgresMigrator(val jdbcTemplateBuilder: JdbcTemplateBuilder) : Migrator {
    override fun run(comparison: Comparison, connection: Connection): MigrationSummary {
        throw NotImplementedError()
    }
}
