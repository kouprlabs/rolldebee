package com.rolldebee.rolldebee.core

interface MigrationScriptBuilder {
    fun build(comparison: Comparison): String
}
