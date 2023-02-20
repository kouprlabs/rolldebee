package com.rolldebee.rolldebee.core

import com.fasterxml.jackson.annotation.JsonInclude
import com.rolldebee.rolldebee.entity.Connection

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ObjectGraph(val nodes: ArrayList<Node> = ArrayList()) {
    data class Node(
        var databaseObject: DatabaseObject,
        var dependencies: List<Node> = ArrayList(),
    ) {
        override fun toString(): String = databaseObject.toString()
    }
}

interface ObjectGraphBuilder {
    fun build(introspection: Introspection, connection: Connection): ObjectGraph
}
