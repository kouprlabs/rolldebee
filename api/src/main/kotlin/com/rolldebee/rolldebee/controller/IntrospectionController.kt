package com.rolldebee.rolldebee.controller

import com.rolldebee.rolldebee.core.Introspection
import com.rolldebee.rolldebee.factory.IntrospectionBuilderFactory
import com.rolldebee.rolldebee.repository.ConnectionRepository
import jakarta.validation.constraints.NotBlank
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("introspections")
class IntrospectionController(
    var introspectionBuilderFactory: IntrospectionBuilderFactory,
    val connectionRepository: ConnectionRepository,
) {
    data class CreateOptions(
        @NotBlank val connectionId: String,
    )

    @PostMapping
    fun run(
        @RequestBody body: CreateOptions,
    ): Introspection {
        val connection = connectionRepository.findById(body.connectionId).get()
        val introspection = introspectionBuilderFactory.get(connection).build(connection)
        introspection.tables = introspection.tables.sortedBy { it.name }
        introspection.tables.forEach { table -> table.columns = table.columns.sortedBy { col -> col.name } }
        introspection.constraints = introspection.constraints.sortedBy { it.name }
        introspection.indexes = introspection.indexes.sortedBy { it.name }
        introspection.sources = introspection.sources.sortedBy { it.name }
        introspection.sequences = introspection.sequences.sortedBy { it.name }
        introspection.materializedViews = introspection.materializedViews.sortedBy { it.name }
        introspection.views = introspection.views.sortedBy { it.name }
        return introspection
    }
}
