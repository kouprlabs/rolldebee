package com.rolldebee.rolldebee.core

import com.rolldebee.rolldebee.entity.Connection

data class EnableTriggerSummary(
    val successes: ArrayList<Success> = ArrayList(),
    val failures: ArrayList<Failure> = ArrayList(),
) {
    data class Success(
        var name: String,
    )

    data class Failure(
        var name: String,
        var reason: String,
    )
}

interface TriggerEnabler {
    fun run(connection: Connection): EnableTriggerSummary
}
