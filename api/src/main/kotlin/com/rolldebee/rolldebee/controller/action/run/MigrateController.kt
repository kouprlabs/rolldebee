package com.rolldebee.rolldebee.controller.action.run

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rolldebee.rolldebee.entity.Action
import com.rolldebee.rolldebee.entity.ActionStatus
import com.rolldebee.rolldebee.entity.ActionType
import com.rolldebee.rolldebee.factory.ComparerFactory
import com.rolldebee.rolldebee.factory.IntrospectionBuilderFactory
import com.rolldebee.rolldebee.factory.MigratorFactory
import com.rolldebee.rolldebee.factory.ObjectGraphBuilderFactory
import com.rolldebee.rolldebee.factory.ObjectRouteBuilderFactory
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
@RequestMapping("actions/run/migrate")
class MigrateController(
    val introspectionBuilderFactory: IntrospectionBuilderFactory,
    val objectGraphBuilderFactory: ObjectGraphBuilderFactory,
    val objectRouteBuilderFactory: ObjectRouteBuilderFactory,
    val connectionRepository: ConnectionRepository,
    val comparerFactory: ComparerFactory,
    val migratorFactory: MigratorFactory,
    val actionService: ActionService,
) {
    data class MigrateOptions(
        @field:NotBlank var sourceConnectionId: String,
        @field:NotBlank var targetConnectionId: String,
    )

    @PostMapping("start/")
    fun migrate(
        @Valid @RequestBody body: MigrateOptions,
    ): Action {
        val actionId = actionService.create(ActionType.MIGRATE, jacksonObjectMapper().writeValueAsString(body))
        GlobalScope.launch {
            try {
                actionService.updateStatus(actionId, ActionStatus.RUNNING)
                val sourceConnection = connectionRepository.findById(body.sourceConnectionId).get()
                val targetConnection = connectionRepository.findById(body.targetConnectionId).get()
                val introspection = introspectionBuilderFactory.get(sourceConnection).build(sourceConnection)
                val objectGraph = objectGraphBuilderFactory.get(sourceConnection).build(introspection, sourceConnection)
                val objectRoute = objectRouteBuilderFactory.get(sourceConnection).build(objectGraph)
                val compare = comparerFactory.get(sourceConnection).build(objectRoute, targetConnection)
                val migrationSummary = migratorFactory.get(targetConnection).run(compare, targetConnection)
                actionService.updateStatus(actionId, ActionStatus.SUCCEEDED)
                actionService.updateResult(actionId, jacksonObjectMapper().writeValueAsString(migrationSummary))
            } catch (e: Exception) {
                actionService.updateStatus(actionId, ActionStatus.FAILED)
                actionService.updateResult(actionId, e.message.toString())
            }
        }
        return actionService.getById(actionId)
    }
}
