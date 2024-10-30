package com.rolldebee.rolldebee.database.mysql

import com.rolldebee.rolldebee.core.Comparison
import com.rolldebee.rolldebee.core.MigrationScriptBuilder
import org.springframework.stereotype.Service

@Service
class MySqlMigrationScriptBuilder : MigrationScriptBuilder {
    override fun build(comparison: Comparison): String = throw NotImplementedError()
}
