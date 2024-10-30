package com.rolldebee.rolldebee.controller.action

import com.rolldebee.rolldebee.entity.Action
import com.rolldebee.rolldebee.entity.ActionStatus
import com.rolldebee.rolldebee.repository.ActionRepository
import com.rolldebee.rolldebee.repository.ConnectionRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

@RestController
@RequestMapping("actions")
class ActionController(
    val actionRepository: ActionRepository,
    val connectionRepository: ConnectionRepository,
) {
    @GetMapping
    fun getAll(
        @RequestParam(value = "type", required = false) type: String?,
    ): List<Action> {
        val actions =
            if (type.isNullOrBlank()) {
                actionRepository.findAll()
            } else {
                actionRepository.findAllByType(type)
            }
        actions.sortByDescending { Date.from(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(it.createTime))) }
        actions.take(100)
        return actions
    }

    @GetMapping("{id}")
    fun getById(
        @PathVariable("id") id: String,
    ): Action = actionRepository.findById(id).get()

    @DeleteMapping("{id}")
    fun delete(
        @PathVariable("id") id: String,
    ): ResponseEntity<Any> {
        val action = actionRepository.findById(id).get()
        return if (action.status != ActionStatus.RUNNING) {
            actionRepository.delete(action)
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.badRequest().build()
        }
    }
}
