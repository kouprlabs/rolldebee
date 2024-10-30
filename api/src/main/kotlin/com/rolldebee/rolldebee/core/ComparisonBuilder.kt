package com.rolldebee.rolldebee.core

import com.rolldebee.rolldebee.entity.Connection

data class Comparison(
    val diffs: List<Diff> = ArrayList(),
) {
    data class Diff(
        var databaseObject: DatabaseObject,
        var type: DiffType,
        var ddls: List<String>,
    )

    enum class DiffType {
        ADDED,
        MODIFIED,
        DELETED,
    }
}

interface ComparisonBuilder {
    fun build(
        objectRoute: ObjectRoute,
        connection: Connection,
    ): Comparison
}
