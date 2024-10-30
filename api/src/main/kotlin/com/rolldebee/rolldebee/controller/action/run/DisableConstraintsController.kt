package com.rolldebee.rolldebee.controller.action.run

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rolldebee.rolldebee.entity.Action
import com.rolldebee.rolldebee.entity.ActionStatus
import com.rolldebee.rolldebee.entity.ActionType
import com.rolldebee.rolldebee.factory.ConstraintDisablerFactory
import com.rolldebee.rolldebee.factory.IntrospectionBuilderFactory
import com.rolldebee.rolldebee.repository.ConnectionRepository
import com.rolldebee.rolldebee.service.ActionService
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@OptIn(DelicateCoroutinesApi::class)
@RestController
@RequestMapping("actions/run/disable_constraints")
class DisableConstraintsController(
    val introspectionBuilderFactory: IntrospectionBuilderFactory,
    val constraintDisablerFactory: ConstraintDisablerFactory,
    val connectionRepository: ConnectionRepository,
    val actionService: ActionService,
) {
    data class DisableConstraintsOptions(
        @field:NotBlank val connectionId: String,
    )

    @PostMapping
    fun run(
        @Valid @RequestBody body: DisableConstraintsOptions,
    ): Action {
        val actionId =
            actionService.create(ActionType.DISABLE_CONSTRAINTS, jacksonObjectMapper().writeValueAsString(body))
        GlobalScope.launch {
            try {
                actionService.updateStatus(actionId, ActionStatus.RUNNING)
                val connection = connectionRepository.findById(body.connectionId).get()
                val switchSummary = constraintDisablerFactory.get(connection).run(connection)
                actionService.updateStatus(actionId, ActionStatus.SUCCEEDED)
                actionService.updateResult(actionId, jacksonObjectMapper().writeValueAsString(switchSummary))
            } catch (e: Exception) {
                actionService.updateStatus(actionId, ActionStatus.FAILED)
                actionService.updateResult(actionId, e.message.toString())
            }
        }
        return actionService.getById(actionId)
    }
}
