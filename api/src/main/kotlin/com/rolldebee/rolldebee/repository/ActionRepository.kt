package com.rolldebee.rolldebee.repository

import com.rolldebee.rolldebee.entity.Action
import org.springframework.data.jpa.repository.JpaRepository

@org.springframework.stereotype.Repository
interface ActionRepository : JpaRepository<Action, String> {
    fun findAllByType(type: String): ArrayList<Action>
}
