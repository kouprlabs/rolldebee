package com.rolldebee.rolldebee.database.postgres

import com.rolldebee.rolldebee.core.ConstraintGraph
import com.rolldebee.rolldebee.core.ConstraintRoute
import com.rolldebee.rolldebee.core.ConstraintRouteBuilder
import org.springframework.stereotype.Service

@Service
class PostgresConstraintRouteBuilder : ConstraintRouteBuilder {
    override fun build(constraintGraph: ConstraintGraph): ConstraintRoute = throw NotImplementedError()
}
