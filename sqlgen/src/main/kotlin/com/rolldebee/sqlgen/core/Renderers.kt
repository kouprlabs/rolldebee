package com.rolldebee.sqlgen.core

class RenderOptions(
    private val indentSize: Int,
    private val continuationIndentSize: Int,
) {
    fun indent(): String {
        return " ".repeat(indentSize)
    }

    fun continuationIndent(): String {
        return " ".repeat(continuationIndentSize)
    }

    fun newLine(): String {
        return "\r\n"
    }
}

interface Renderer {
    fun render(options: RenderOptions): String
}

interface ColumnTypeRenderer : Renderer {
    fun name(): String
}

interface TabularObjectRenderer : Renderer {
    fun name(): String
}