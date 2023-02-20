package com.rolldebee.rolldebee.database.mysql

import com.rolldebee.rolldebee.core.Comparison
import com.rolldebee.rolldebee.core.ComparisonBuilder
import com.rolldebee.rolldebee.core.ObjectRoute
import com.rolldebee.rolldebee.entity.Connection
import org.springframework.stereotype.Service

@Service
class MySqlComparisonBuilder : ComparisonBuilder {
    override fun build(objectRoute: ObjectRoute, connection: Connection): Comparison {
        throw NotImplementedError()
    }
}
