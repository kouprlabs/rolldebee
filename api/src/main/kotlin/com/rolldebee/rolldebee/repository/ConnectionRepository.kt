package com.rolldebee.rolldebee.repository

import com.rolldebee.rolldebee.entity.Connection
import org.springframework.data.jpa.repository.JpaRepository

@org.springframework.stereotype.Repository
interface ConnectionRepository : JpaRepository<Connection, String>
