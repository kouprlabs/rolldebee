package com.rolldebee.rolldebee.service

import com.rolldebee.rolldebee.entity.Action
import com.rolldebee.rolldebee.entity.ActionStatus
import com.rolldebee.rolldebee.infra.uuid
import com.rolldebee.rolldebee.repository.ActionRepository
import org.springframework.stereotype.Service

@Service
class ActionService(val actionRepository: ActionRepository) {
    fun create(type: String, params: String): String {
        val id = uuid()
        actionRepository.save(
            Action(
                id = id, type = type, params = params, status = ActionStatus.PENDING
            )
        )
        return id
    }

    fun getById(id: String): Action {
        return actionRepository.findById(id).get()
    }

    fun updateStatus(id: String, status: String) {
        val action = actionRepository.findById(id).get()
        action.status = status
        actionRepository.save(action)
    }

    fun updateResult(id: String, result: String) {
        val action = actionRepository.findById(id).get()
        action.result = result
        actionRepository.save(action)
    }
}
