package com.codemakers.aquaplus.ui.extensions

import java.time.LocalDate

fun String?.unescapeNewlines(): String =
    this?.replace("\\r\\n", "\n")?.replace("\\n", "\n").orEmpty()

fun String.toCapitalCase(): String =
    split(" ").joinToString(" ") {
        it.lowercase().replaceFirstChar { char -> char.titlecase() }
    }

fun String.toLocalDate(): LocalDate? = try {
    val localDate = LocalDate.parse(this)
    localDate
} catch (e: Exception) {
    null
}