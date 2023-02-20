package com.rolldebee.rolldebee.core

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Column(
    var name: String,
    var tableName: String,
    var columnType: String,
    var order: Long,
    var comments: String?,
    var dataLength: Long,
    var dataPrecision: Long,
    var dataScale: Long,
    var nullable: Boolean,
    var ddl: String
) {
    val id: String
        get() = "column:${tableName.lowercase()}:${name.lowercase()}"

    override fun toString(): String = "$tableName.$name"
}

interface DatabaseObject {
    val id: String
        get() = objectType.lowercase() + ":" + name.lowercase()
    val objectType: String
    val name: String
    val ddl: String
    val dropDdl: String
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Constraint(
    override var name: String,
    override var ddl: String,
    override var dropDdl: String,
    override val objectType: String,
    var constraintType: String,
    var tableName: String,
    var status: String,
    var validated: String,
    var refConstraintName: String?,
) : DatabaseObject {
    override fun toString(): String = name
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Index(
    override var name: String,
    override var ddl: String,
    override var dropDdl: String,
    var indexType: String,
    var status: String,
) : DatabaseObject {
    override val objectType: String
        get() = "INDEX"

    override fun toString(): String = name
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class MaterializedView(
    override var name: String,
    override var ddl: String,
    override var dropDdl: String,
) : DatabaseObject {
    override val objectType: String
        get() = "MATERIALIZED VIEW"

    override fun toString(): String = name
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Sequence(
    override var name: String,
    override var ddl: String,
    override var dropDdl: String,
) : DatabaseObject {
    override val objectType: String
        get() = "SEQUENCE"

    override fun toString(): String = name
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Source(
    override var name: String,
    override var ddl: String,
    override var dropDdl: String,
    override val objectType: String,
    val status: String,
) : DatabaseObject {
    override fun toString(): String = name
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Table(
    override var name: String,
    override var ddl: String,
    override var dropDdl: String,
    var comments: String?,
    var columns: List<Column> = ArrayList(),
) : DatabaseObject {
    override val objectType: String
        get() = "TABLE"

    override fun toString(): String = name
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class View(
    override var name: String,
    override var ddl: String,
    override var dropDdl: String,
) : DatabaseObject {
    override val objectType: String
        get() = "VIEW"

    override fun toString(): String = name
}
