package com.rolldebee.rolldebee.database.mysql

import com.rolldebee.rolldebee.core.DisableTriggerSummary
import com.rolldebee.rolldebee.core.TriggerDisabler
import com.rolldebee.rolldebee.entity.Connection
import org.springframework.stereotype.Service

@Service
class MySqlTriggerDisabler : TriggerDisabler {
    override fun run(connection: Connection): DisableTriggerSummary {
        throw NotImplementedError()
    }
}
