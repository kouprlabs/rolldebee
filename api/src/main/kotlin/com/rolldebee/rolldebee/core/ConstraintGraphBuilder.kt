package com.rolldebee.rolldebee.core

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ConstraintGraph(
    val nodes: ArrayList<Node> = ArrayList(),
) {
    data class Node(
        var constraint: Constraint,
        var dependencies: List<Node> = ArrayList(),
    ) {
        override fun toString(): String = constraint.toString()
    }
}

interface ConstraintGraphBuilder {
    fun build(jdbcTemplate: NamedParameterJdbcTemplate): ConstraintGraph
}
