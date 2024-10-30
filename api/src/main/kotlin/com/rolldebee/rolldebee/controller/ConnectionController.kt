package com.rolldebee.rolldebee.controller

import com.rolldebee.rolldebee.entity.Connection
import com.rolldebee.rolldebee.infra.uuid
import com.rolldebee.rolldebee.repository.ConnectionRepository
import org.springframework.web.bind.annotation.*
import jakarta.validation.constraints.NotBlank

@RestController
@RequestMapping("connections")
class ConnectionController(val repository: ConnectionRepository) {
    data class CreateOptions(
        @NotBlank val name: String,
        @NotBlank val jdbcUrl: String,
        @NotBlank val jdbcUsername: String,
        val jdbcPassword: String?,
        @NotBlank val databaseType: String,
    )

    @PostMapping
    fun create(@RequestBody body: CreateOptions): Connection {
        val id = uuid()
        repository.save(
            Connection(
                id = id,
                name = body.name,
                jdbcUrl = body.jdbcUrl,
                jdbcUsername = body.jdbcUsername,
                jdbcPassword = body.jdbcPassword,
                databaseType = body.databaseType,
            )
        )
        return repository.getById(id)
    }

    @GetMapping
    suspend fun getAll(): List<Connection> {
        return repository.findAll()
    }

    @GetMapping("{id}")
    fun getById(@PathVariable id: String): Connection {
        return repository.getById(id)
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: String) {
        repository.delete(repository.getById(id))
    }

    data class UpdateOptions(
        @NotBlank val name: String,
        @NotBlank val jdbcUrl: String,
        @NotBlank val jdbcUsername: String,
        val jdbcPassword: String?,
    )

    @PatchMapping("{id}")
    fun update(@PathVariable id: String, @RequestBody body: UpdateOptions): Connection {
        val connection = repository.getById(id)
        connection.name = body.name
        connection.jdbcUrl = body.jdbcUrl
        connection.jdbcUsername = body.jdbcUsername
        connection.jdbcPassword = body.jdbcPassword
        repository.save(connection)
        return repository.getById(id)
    }
}
