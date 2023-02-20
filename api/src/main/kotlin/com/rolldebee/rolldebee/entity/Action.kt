package com.rolldebee.rolldebee.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.rolldebee.rolldebee.infra.uuid
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.PreUpdate

object ActionType {
    const val CLONE = "clone"
    const val MIGRATE = "migrate"
    const val ENABLE_CONSTRAINTS = "enable_constraints"
    const val DISABLE_CONSTRAINTS = "disable_constraints"
    const val ENABLE_TRIGGERS = "enable_triggers"
    const val DISABLE_TRIGGERS = "disable_triggers"
    const val DROP_OBJECTS = "drop_objects"
}

object ActionStatus {
    const val PENDING = "pending"
    const val RUNNING = "running"
    const val SUCCEEDED = "succeeded"
    const val FAILED = "failed"
}

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Action(
    @Id @Column(columnDefinition = "text") var id: String = uuid(),
    @Column(columnDefinition = "text") var type: String,
    @Column(columnDefinition = "text", nullable = true) var status: String,
    @Column(columnDefinition = "text", nullable = true) var params: String? = null,
    @Column(columnDefinition = "text", nullable = true) var result: String? = null,
    @Column(columnDefinition = "text") var createTime: String = OffsetDateTime.now(ZoneOffset.UTC)
        .format(DateTimeFormatter.ISO_INSTANT),
    @Column(columnDefinition = "text", nullable = true) var updateTime: String? = null,
) {
    @PreUpdate
    fun preUpdate() {
        updateTime = OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
    }
}
