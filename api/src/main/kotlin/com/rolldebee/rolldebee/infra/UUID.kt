package com.rolldebee.rolldebee.infra

import org.sqids.Sqids
import java.util.UUID

fun uuid(): String {
    val uuid = UUID.randomUUID()
    return Sqids.builder().build().encode(
        listOf(
            uuid.mostSignificantBits,
            uuid.leastSignificantBits,
            System.currentTimeMillis(),
        ),
    )
}
