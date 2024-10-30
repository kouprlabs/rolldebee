package com.rolldebee.rolldebee.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.rolldebee.rolldebee.infra.uuid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.PreUpdate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object DatabaseType {
    const val POSTGRES = "postgres"
    const val MYSQL = "mysql"
    const val RED = "red"
}

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Connection(
    @Id @Column(columnDefinition = "text") var id: String = uuid(),
    @Column(columnDefinition = "text") var name: String,
    @Column(columnDefinition = "text") var jdbcUrl: String,
    @Column(columnDefinition = "text") var jdbcUsername: String,
    @Column(columnDefinition = "text") var jdbcPassword: String? = null,
    @Column(columnDefinition = "text") var databaseType: String,
    @Column(columnDefinition = "text") var createTime: String =
        OffsetDateTime
            .now(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_INSTANT),
    @Column(columnDefinition = "text", nullable = true) var updateTime: String? = null,
) {
    @PreUpdate
    fun preUpdate() {
        updateTime = OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
    }
}
