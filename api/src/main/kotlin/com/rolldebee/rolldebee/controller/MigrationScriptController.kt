package com.rolldebee.rolldebee.controller

import com.rolldebee.rolldebee.factory.*
import com.rolldebee.rolldebee.repository.ConnectionRepository
import jakarta.validation.constraints.NotBlank
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("migration_scripts")
class MigrationScriptController(
    val introspectionBuilderFactory: IntrospectionBuilderFactory,
    val objectGraphBuilderFactory: ObjectGraphBuilderFactory,
    val objectRouteBuilderFactory: ObjectRouteBuilderFactory,
    val comparerFactory: ComparerFactory,
    val migrationScriptBuilderFactory: MigrationScriptBuilderFactory,
    val connectionRepository: ConnectionRepository,
) {
    data class Options(
        @NotBlank var sourceConnectionId: String,
        @NotBlank var targetConnectionId: String,
    )

    @PostMapping(produces = ["text/plain"])
    fun create(
        @RequestBody body: Options,
    ): String {
        val sourceConnection = connectionRepository.findById(body.sourceConnectionId).get()
        val targetConnection = connectionRepository.findById(body.targetConnectionId).get()
        val sourceIntrospection = introspectionBuilderFactory.get(sourceConnection).build(sourceConnection)
        val sourceGraph = objectGraphBuilderFactory.get(sourceConnection).build(sourceIntrospection, sourceConnection)
        val sourceRoute = objectRouteBuilderFactory.get(sourceConnection).build(sourceGraph)
        val comparison = comparerFactory.get(targetConnection).build(sourceRoute, targetConnection)
        return migrationScriptBuilderFactory.get(targetConnection).build(comparison)
    }
}
