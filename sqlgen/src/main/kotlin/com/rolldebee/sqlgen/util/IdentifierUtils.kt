package com.rolldebee.sqlgen.core

private const val MAX_LENGTH = 30

fun sanitize(value: String): String {
    var sanitized =
        value
            .trim { it <= ' ' }
            .lowercase()
            .replace(" ", "_")
            .replace("[^a-zA-Z_]".toRegex(), "")
    sanitized = shorten(sanitized, 30)
    return sanitized
}

fun shorten(
    value: String,
    maxLength: Int,
): String {
    var result = value
    if (result.length > maxLength) {
        result = result.substring(0, maxLength)
        if (result.endsWith("_")) {
            result.replace("_$".toRegex(), "")
        }
    }
    return result
}

fun prefix(
    prefix: String,
    value: String,
): String = prefix + shorten(value, MAX_LENGTH - prefix.length)

fun suffix(
    value: String,
    suffix: String,
): String = shorten(value, MAX_LENGTH - suffix.length) + suffix

typealias NextValue = () -> String

fun unique(
    used: List<String>,
    nextValue: NextValue,
): String {
    while (true) {
        val name = sanitize(nextValue())
        if (used.stream().noneMatch { e: String -> e.equals(name, ignoreCase = true) }) {
            return name
        }
    }
}
