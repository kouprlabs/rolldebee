package com.rolldebee.rolldebee.database.red

import com.rolldebee.rolldebee.core.ObjectGraph
import com.rolldebee.rolldebee.core.ObjectRoute
import com.rolldebee.rolldebee.core.ObjectRouteBuilder
import org.springframework.stereotype.Service

@Service
class RedObjectRouteBuilder : ObjectRouteBuilder {
    override fun build(objectGraph: ObjectGraph): ObjectRoute {
        val nodes = ArrayList<ObjectGraph.Node>()
        val crossed = ArrayList<ObjectGraph.Node>()
        val constraints: List<ObjectGraph.Node> =
            objectGraph.nodes.filter { it.databaseObject.objectType == "CONSTRAINT" }
        val refConstraints: List<ObjectGraph.Node> =
            objectGraph.nodes.filter { it.databaseObject.objectType == "REF_CONSTRAINT" }
        val indexes: List<ObjectGraph.Node> = objectGraph.nodes.filter { it.databaseObject.objectType == "INDEX" }
        val filtered = ArrayList(objectGraph.nodes)
        filtered.removeAll(constraints.toSet())
        filtered.removeAll(refConstraints.toSet())
        filtered.removeAll(indexes.toSet())
        for (node in filtered) {
            append(node, nodes, crossed)
        }
        nodes.addAll(constraints)
        nodes.addAll(indexes)
        nodes.addAll(refConstraints)
        return ObjectRoute(databaseObjects = nodes.map { it.databaseObject })
    }

    private fun append(
        node: ObjectGraph.Node,
        nodes: ArrayList<ObjectGraph.Node>,
        crossed: ArrayList<ObjectGraph.Node>,
    ) {
        if (nodes.contains(node)) {
            return
        }
        if (crossed.contains(node)) {
            return
        }
        crossed.add(node)
        for (child in node.dependencies) {
            append(child, nodes, crossed)
        }
        nodes.add(node)
    }
}
