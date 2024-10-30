package com.rolldebee.rolldebee.core

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ObjectRoute(
    var databaseObjects: List<DatabaseObject> = ArrayList(),
)

interface ObjectRouteBuilder {
    fun build(objectGraph: ObjectGraph): ObjectRoute
}
