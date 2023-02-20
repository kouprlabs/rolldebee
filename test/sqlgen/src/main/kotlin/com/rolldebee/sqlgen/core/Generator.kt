package com.rolldebee.sqlgen.core

interface Generator {
    fun generate(): List<Renderer>
}