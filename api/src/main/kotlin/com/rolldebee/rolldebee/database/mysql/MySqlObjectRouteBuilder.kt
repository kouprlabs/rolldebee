package com.rolldebee.rolldebee.database.mysql

import com.rolldebee.rolldebee.core.ObjectGraph
import com.rolldebee.rolldebee.core.ObjectRoute
import com.rolldebee.rolldebee.core.ObjectRouteBuilder
import org.springframework.stereotype.Service

@Service
class MySqlObjectRouteBuilder : ObjectRouteBuilder {
    override fun build(objectGraph: ObjectGraph): ObjectRoute = throw NotImplementedError()
}
