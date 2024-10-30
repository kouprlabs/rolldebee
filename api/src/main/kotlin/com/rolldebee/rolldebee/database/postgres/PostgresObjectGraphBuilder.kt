package com.rolldebee.rolldebee.database.postgres

import com.rolldebee.rolldebee.core.Introspection
import com.rolldebee.rolldebee.core.ObjectGraph
import com.rolldebee.rolldebee.core.ObjectGraphBuilder
import com.rolldebee.rolldebee.entity.Connection
import org.springframework.stereotype.Service

@Service
class PostgresObjectGraphBuilder : ObjectGraphBuilder {
    override fun build(
        introspection: Introspection,
        connection: Connection,
    ): ObjectGraph = throw NotImplementedError()
}
