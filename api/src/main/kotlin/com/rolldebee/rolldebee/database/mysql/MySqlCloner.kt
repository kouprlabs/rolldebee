package com.rolldebee.rolldebee.database.mysql

import com.rolldebee.rolldebee.core.Cloner
import com.rolldebee.rolldebee.core.CloningSummary
import com.rolldebee.rolldebee.core.ObjectRoute
import com.rolldebee.rolldebee.entity.Connection
import org.springframework.stereotype.Service

@Service
class MySqlCloner : Cloner {
    override fun run(
        objectRoute: ObjectRoute,
        connection: Connection,
    ): CloningSummary = throw NotImplementedError()
}
