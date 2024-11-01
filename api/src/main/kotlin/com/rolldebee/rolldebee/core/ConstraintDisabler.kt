package com.rolldebee.rolldebee.core

import com.rolldebee.rolldebee.entity.Connection

data class DisableConstraintSummary(
    val successes: ArrayList<Success> = ArrayList(),
    val failures: ArrayList<Failure> = ArrayList(),
) {
    data class Success(
        var tableName: String,
        var constraintName: String,
    )

    data class Failure(
        var tableName: String,
        var constraintName: String,
        var reason: String,
    )
}

interface ConstraintDisabler {
    fun run(connection: Connection): DisableConstraintSummary
}
