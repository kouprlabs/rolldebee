package com.rolldebee.rolldebee.core

import com.rolldebee.rolldebee.entity.Connection

data class MigrationSummary(
    val failures: List<Failure> = ArrayList(),
    val succeeded: List<Comparison.Diff> = ArrayList()
) {
    data class Failure(
        val diff: Comparison.Diff,
        val ddls: List<Ddl> = ArrayList(),
    ) {
        data class Ddl(var ddl: String, var error: String)
    }
}

interface Migrator {
    fun run(comparison: Comparison, connection: Connection): MigrationSummary
}
