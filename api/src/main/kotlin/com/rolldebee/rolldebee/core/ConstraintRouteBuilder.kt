package com.rolldebee.rolldebee.core

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ConstraintRoute(
    var constraints: List<Constraint> = ArrayList(),
)

interface ConstraintRouteBuilder {
    fun build(constraintGraph: ConstraintGraph): ConstraintRoute
}
