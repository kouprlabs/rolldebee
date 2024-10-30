package com.rolldebee.rolldebee.database.mysql

import com.rolldebee.rolldebee.core.ConstraintDisabler
import com.rolldebee.rolldebee.core.DisableConstraintSummary
import com.rolldebee.rolldebee.entity.Connection
import org.springframework.stereotype.Service

@Service
class MySqlConstraintDisabler : ConstraintDisabler {
    override fun run(connection: Connection): DisableConstraintSummary = throw NotImplementedError()
}
