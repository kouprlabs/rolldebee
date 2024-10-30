package com.rolldebee.rolldebee.database.red

import com.rolldebee.rolldebee.core.ConstraintGraph
import com.rolldebee.rolldebee.core.ConstraintRoute
import com.rolldebee.rolldebee.core.ConstraintRouteBuilder
import org.springframework.stereotype.Service

@Service
class RedConstraintRouteBuilder : ConstraintRouteBuilder {
    override fun build(constraintGraph: ConstraintGraph): ConstraintRoute {
        val nodes = ArrayList<ConstraintGraph.Node>()
        for (node in constraintGraph.nodes) {
            recurse(nodes, node)
        }
        return ConstraintRoute(constraints = nodes.map { it.constraint })
    }

    private fun recurse(
        nodes: ArrayList<ConstraintGraph.Node>,
        node: ConstraintGraph.Node,
    ) {
        if (nodes.any { it.constraint.name == node.constraint.name }) {
            return
        }
        for (dependency in node.dependencies) {
            recurse(nodes, dependency)
        }
        nodes.add(node)
    }
}
