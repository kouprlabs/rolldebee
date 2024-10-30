package com.rolldebee.rolldebee.controller.action.run

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rolldebee.rolldebee.entity.Action
import com.rolldebee.rolldebee.entity.ActionStatus
import com.rolldebee.rolldebee.entity.ActionType
import com.rolldebee.rolldebee.factory.ClonerFactory
import com.rolldebee.rolldebee.factory.IntrospectionBuilderFactory
import com.rolldebee.rolldebee.factory.ObjectGraphBuilderFactory
import com.rolldebee.rolldebee.factory.ObjectRouteBuilderFactory
import com.rolldebee.rolldebee.repository.ConnectionRepository
import com.rolldebee.rolldebee.service.ActionService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank

@OptIn(DelicateCoroutinesApi::class)
@RestController
@RequestMapping("actions/run/clone")
class CloneController(
    val introspectionBuilderFactory: IntrospectionBuilderFactory,
    val objectGraphBuilderFactory: ObjectGraphBuilderFactory,
    val objectRouteBuilderFactory: ObjectRouteBuilderFactory,
    val connectionRepository: ConnectionRepository,
    val clonerFactory: ClonerFactory,
    val actionService: ActionService,
) {
    data class CloneOptions(
        @field:NotBlank var sourceConnectionId: String,
        @field:NotBlank var targetConnectionId: String,
    )

    @PostMapping
    fun run(@Valid @RequestBody body: CloneOptions): Action {
        val actionId = actionService.create(ActionType.CLONE, jacksonObjectMapper().writeValueAsString(body))
        GlobalScope.launch {
            try {
                actionService.updateStatus(actionId, ActionStatus.RUNNING)
                val sourceConnection = connectionRepository.getById(body.sourceConnectionId)
                val targetConnection = connectionRepository.getById(body.targetConnectionId)
                val introspection = introspectionBuilderFactory.get(sourceConnection).build(sourceConnection)
                val objectGraph = objectGraphBuilderFactory.get(sourceConnection).build(introspection, sourceConnection)
                val objectRoute = objectRouteBuilderFactory.get(sourceConnection).build(objectGraph)
                val cloneResult = clonerFactory.get(targetConnection).run(objectRoute, targetConnection)
                actionService.updateStatus(actionId, ActionStatus.SUCCEEDED)
                actionService.updateResult(actionId, jacksonObjectMapper().writeValueAsString(cloneResult))
            } catch (e: Exception) {
                actionService.updateResult(actionId, e.message.toString())
                actionService.updateStatus(actionId, ActionStatus.FAILED)
            }
        }
        return actionService.getById(actionId)
    }
}
