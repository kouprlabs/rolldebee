package com.rolldebee.rolldebee.database.mysql

import com.rolldebee.rolldebee.core.EnableConstraintsSummary
import com.rolldebee.rolldebee.core.ConstraintEnabler
import com.rolldebee.rolldebee.entity.Connection
import org.springframework.stereotype.Service

@Service
class MySqlConstraintEnabler : ConstraintEnabler {
    override fun run(connection: Connection): EnableConstraintsSummary {
        throw NotImplementedError()
    }
}
