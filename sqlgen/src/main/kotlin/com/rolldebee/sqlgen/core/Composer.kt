package com.rolldebee.sqlgen.core

data class ComposeOptions(
    val tableCount: Int,
    val foreignKeyMaxCount: Int,
    val viewCount: Int,
    val materializedViewCount: Int,
    val indentSize: Int,
    val continuationIndentSize: Int,
)

interface Composer {
    fun compose(options: ComposeOptions): String
}