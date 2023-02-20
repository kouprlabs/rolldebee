package com.rolldebee.rolldebee.core

import com.rolldebee.rolldebee.entity.Connection

data class Introspection(
    var tables: List<Table> = ArrayList(),
    var constraints: List<Constraint> = ArrayList(),
    var indexes: List<Index> = ArrayList(),
    var materializedViews: List<MaterializedView> = ArrayList(),
    var views: List<View> = ArrayList(),
    var sequences: List<Sequence> = ArrayList(),
    var sources: List<Source> = ArrayList(),
)

interface IntrospectionBuilder {
    fun build(connection: Connection): Introspection
}
