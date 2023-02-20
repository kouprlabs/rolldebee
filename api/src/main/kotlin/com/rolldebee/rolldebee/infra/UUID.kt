package com.rolldebee.rolldebee.infra

import org.hashids.Hashids
import java.util.*

fun uuid(): String {
    val hashids = Hashids(UUID.randomUUID().toString())
    return hashids.encode(System.currentTimeMillis())
}
