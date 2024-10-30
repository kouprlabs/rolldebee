package com.rolldebee.rolldebee.controller.action.run

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rolldebee.rolldebee.entity.Action
import com.rolldebee.rolldebee.entity.ActionStatus
import com.rolldebee.rolldebee.entity.ActionType
import com.rolldebee.rolldebee.factory.IntrospectionBuilderFactory
import com.rolldebee.rolldebee.factory.ObjectDropperFactory
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
@RequestMapping("actions/run/drop_objects")
class DropObjectsController(
    val introspectionBuilderFactory: IntrospectionBuilderFactory,
    val objectGraphBuilderFactory: ObjectGraphBuilderFactory,
    val objectRouteBuilderFactory: ObjectRouteBuilderFactory,
    val connectionRepository: ConnectionRepository,
    val objectDropperFactory: ObjectDropperFactory,
    val actionService: ActionService,
) {
    data class DropObjectsOptions(
        @field:NotBlank var connectionId: String,
    )

    @PostMapping
    fun run(@Valid @RequestBody body: DropObjectsOptions): Action {
        val actionId = actionService.create(ActionType.DROP_OBJECTS, jacksonObjectMapper().writeValueAsString(body))
        GlobalScope.launch {
            try {
                actionService.updateStatus(actionId, ActionStatus.RUNNING)
                val connection = connectionRepository.getById(body.connectionId)
                val introspection = introspectionBuilderFactory.get(connection).build(connection)
                val objectGraph = objectGraphBuilderFactory.get(connection).build(introspection, connection)
                val objectRoute = objectRouteBuilderFactory.get(connection).build(objectGraph)
                val dropSummary = objectDropperFactory.get(connection).run(objectRoute, connection)
                actionService.updateStatus(actionId, ActionStatus.SUCCEEDED)
                actionService.updateResult(actionId, jacksonObjectMapper().writeValueAsString(dropSummary))
            } catch (e: Exception) {
                actionService.updateResult(actionId, e.message.toString())
                actionService.updateStatus(actionId, ActionStatus.FAILED)
            }
        }
        return actionService.getById(actionId)
    }
}
