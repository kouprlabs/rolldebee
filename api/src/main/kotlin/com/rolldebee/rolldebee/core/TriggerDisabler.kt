package com.rolldebee.rolldebee.core

import com.rolldebee.rolldebee.entity.Connection

data class DisableTriggerSummary(
    val successes: ArrayList<Success> = ArrayList(),
    val failures: ArrayList<Failure> = ArrayList(),
) {
    data class Success(var name: String)
    data class Failure(var name: String, var reason: String)
}

interface TriggerDisabler {
    fun run(connection: Connection): DisableTriggerSummary
}
