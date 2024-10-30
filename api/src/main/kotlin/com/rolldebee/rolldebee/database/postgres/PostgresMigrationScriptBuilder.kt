package com.rolldebee.rolldebee.database.postgres

import com.rolldebee.rolldebee.core.Comparison
import com.rolldebee.rolldebee.core.MigrationScriptBuilder
import org.springframework.stereotype.Service

@Service
class PostgresMigrationScriptBuilder : MigrationScriptBuilder {
    override fun build(comparison: Comparison): String = throw NotImplementedError()
}
