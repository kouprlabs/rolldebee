package com.rolldebee.rolldebee.core

import com.rolldebee.rolldebee.entity.Connection

data class ObjectDropSummary(
    val successes: ArrayList<DatabaseObject> = ArrayList(),
    val failures: ArrayList<Failure> = ArrayList(),
) {
    data class Failure(var databaseObject: DatabaseObject, var reason: String)
}

interface ObjectDropper {
    fun run(objectRoute: ObjectRoute, connection: Connection): ObjectDropSummary
}