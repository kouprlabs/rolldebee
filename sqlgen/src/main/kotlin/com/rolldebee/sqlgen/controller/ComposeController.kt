package com.rolldebee.sqlgen.controller

import com.rolldebee.sqlgen.core.ComposeOptions
import com.rolldebee.sqlgen.core.Composer
import com.rolldebee.sqlgen.mysql.MySqlComposer
import com.rolldebee.sqlgen.red.RedComposer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("compose")
class ComposeController() {

    @GetMapping(produces = ["text/plain"])
    fun compose(
        @RequestParam("db") db: String,
        @RequestParam("tableCount", defaultValue = "5", required = false) tableCount: Int,
        @RequestParam("foreignKeyMaxCount", defaultValue = "3", required = false) foreignKeyMaxCount: Int,
        @RequestParam("viewCount", defaultValue = "5", required = false) viewCount: Int,
        @RequestParam("materializedViewCount", defaultValue = "5", required = false) materializedViewCount: Int,
        @RequestParam("indentSize", defaultValue = "4", required = false) indentSize: Int,
        @RequestParam("continuationIndentSize", defaultValue = "8", required = false) continuationIndentSize: Int,
    ): String {
        val options = ComposeOptions(
            tableCount = tableCount,
            viewCount = viewCount,
            materializedViewCount = materializedViewCount,
            foreignKeyMaxCount = foreignKeyMaxCount,
            indentSize = indentSize,
            continuationIndentSize = continuationIndentSize,
        )
        val composer: Composer = when (db) {
            "mysql" -> MySqlComposer()
            "red" -> RedComposer()
            else -> throw IllegalArgumentException()
        }
        return composer.compose(options)
    }
}