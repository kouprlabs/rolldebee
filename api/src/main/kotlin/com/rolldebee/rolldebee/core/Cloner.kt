package com.rolldebee.rolldebee.core

import com.rolldebee.rolldebee.entity.Connection

data class CloningSummary(
    val successes: List<DatabaseObject> = ArrayList(),
    val failures: List<Failure> = ArrayList(),
) {
    data class Failure(
        var databaseObject: DatabaseObject,
        var reason: String,
    )
}

interface Cloner {
    fun run(
        objectRoute: ObjectRoute,
        connection: Connection,
    ): CloningSummary
}
