package com.rolldebee.rolldebee.database.postgres

import com.rolldebee.rolldebee.core.DisableTriggerSummary
import com.rolldebee.rolldebee.core.TriggerDisabler
import com.rolldebee.rolldebee.entity.Connection
import org.springframework.stereotype.Service

@Service
class PostgresTriggerDisabler : TriggerDisabler {
    override fun run(connection: Connection): DisableTriggerSummary {
        throw NotImplementedError()
    }
}
