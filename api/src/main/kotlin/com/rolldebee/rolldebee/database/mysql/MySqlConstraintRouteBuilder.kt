package com.rolldebee.rolldebee.database.mysql

import com.rolldebee.rolldebee.core.ConstraintGraph
import com.rolldebee.rolldebee.core.ConstraintRoute
import com.rolldebee.rolldebee.core.ConstraintRouteBuilder
import org.springframework.stereotype.Service

@Service
class MySqlConstraintRouteBuilder : ConstraintRouteBuilder {
    override fun build(constraintGraph: ConstraintGraph): ConstraintRoute = throw NotImplementedError()
}
