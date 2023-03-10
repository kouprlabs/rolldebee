package com.rolldebee.rolldebee.controller

import com.rolldebee.rolldebee.core.Comparison
import com.rolldebee.rolldebee.factory.ComparerFactory
import com.rolldebee.rolldebee.factory.IntrospectionBuilderFactory
import com.rolldebee.rolldebee.factory.ObjectGraphBuilderFactory
import com.rolldebee.rolldebee.factory.ObjectRouteBuilderFactory
import com.rolldebee.rolldebee.repository.ConnectionRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping("comparisons")
class ComparisonController(
    val introspectionBuilderFactory: IntrospectionBuilderFactory,
    val objectGraphBuilderFactory: ObjectGraphBuilderFactory,
    val objectRouteBuilderFactory: ObjectRouteBuilderFactory,
    val comparerFactory: ComparerFactory,
    val connectionRepository: ConnectionRepository,
) {
    data class CreateOptions(
        @NotBlank var sourceConnectionId: String,
        @NotBlank var targetConnectionId: String,
    )

    @PostMapping
    fun run(@RequestBody body: CreateOptions): Comparison {
        val sourceConnection = connectionRepository.getById(body.sourceConnectionId)
        val targetConnection = connectionRepository.getById(body.targetConnectionId)
        val introspection = introspectionBuilderFactory.get(sourceConnection).build(sourceConnection)
        val objectGraph = objectGraphBuilderFactory.get(sourceConnection).build(introspection, sourceConnection)
        val objectRoute = objectRouteBuilderFactory.get(sourceConnection).build(objectGraph)
        return comparerFactory.get(targetConnection).build(objectRoute, targetConnection)
    }
}
