package com.rolldebee.rolldebee.database.postgres

import com.rolldebee.rolldebee.core.Introspection
import com.rolldebee.rolldebee.core.IntrospectionBuilder
import com.rolldebee.rolldebee.entity.Connection
import org.springframework.stereotype.Service

@Service
class PostgresIntrospectionBuilder : IntrospectionBuilder {
    override fun build(connection: Connection): Introspection = throw NotImplementedError()
}
