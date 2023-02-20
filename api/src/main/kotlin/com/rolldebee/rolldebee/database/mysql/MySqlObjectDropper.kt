package com.rolldebee.rolldebee.database.mysql

import com.rolldebee.rolldebee.core.ObjectDropSummary
import com.rolldebee.rolldebee.core.ObjectDropper
import com.rolldebee.rolldebee.core.ObjectRoute
import com.rolldebee.rolldebee.entity.Connection
import org.springframework.stereotype.Service

@Service
class MySqlObjectDropper : ObjectDropper {
    override fun run(objectRoute: ObjectRoute, connection: Connection): ObjectDropSummary {
        throw NotImplementedError()
    }
}
