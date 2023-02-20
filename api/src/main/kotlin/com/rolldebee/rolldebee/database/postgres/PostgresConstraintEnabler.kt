package com.rolldebee.rolldebee.database.postgres

import com.rolldebee.rolldebee.core.EnableConstraintsSummary
import com.rolldebee.rolldebee.core.ConstraintEnabler
import com.rolldebee.rolldebee.entity.Connection
import org.springframework.stereotype.Service

@Service
class PostgresConstraintEnabler : ConstraintEnabler {
    override fun run(connection: Connection): EnableConstraintsSummary {
        throw NotImplementedError()
    }
}
