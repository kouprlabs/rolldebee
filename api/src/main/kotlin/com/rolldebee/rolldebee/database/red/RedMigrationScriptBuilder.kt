package com.rolldebee.rolldebee.database.red

import com.rolldebee.rolldebee.core.Comparison
import com.rolldebee.rolldebee.core.MigrationScriptBuilder
import com.rolldebee.rolldebee.core.Source
import org.springframework.stereotype.Service

@Service
class RedMigrationScriptBuilder : MigrationScriptBuilder {
    override fun build(comparison: Comparison): String {
        val stringBuilder = StringBuilder()
        for (syncOp in comparison.diffs) {
            for (ddl in syncOp.ddls) {
                stringBuilder.append(ddl)
                if (!ddl.endsWith(";")) {
                    stringBuilder.append(";")
                }
                if (syncOp.databaseObject is Source && !ddl.startsWith("DROP")) {
                    stringBuilder.append("\n/")
                }
                stringBuilder.append("\n")
            }
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }
}
