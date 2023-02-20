package com.rolldebee.rolldebee.database.mysql

import com.rolldebee.rolldebee.core.EnableTriggerSummary
import com.rolldebee.rolldebee.core.TriggerEnabler
import com.rolldebee.rolldebee.entity.Connection
import org.springframework.stereotype.Service

@Service
class MySqlTriggerEnabler : TriggerEnabler {
    override fun run(connection: Connection): EnableTriggerSummary {
        throw NotImplementedError()
    }
}
