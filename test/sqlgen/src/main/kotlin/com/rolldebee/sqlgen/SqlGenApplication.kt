package com.rolldebee.sqlgen

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SqlGenApplication

fun main(args: Array<String>) {
    runApplication<SqlGenApplication>(*args)
}
