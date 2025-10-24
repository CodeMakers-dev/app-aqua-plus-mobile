package com.codemakers.aquaplus.ui.extensions

fun String?.unescapeNewlines(): String =
    this?.replace("\\r\\n", "\n")?.replace("\\n", "\n").orEmpty()

fun String.toCapitalCase(): String =
    split(" ").joinToString(" ") {
        it.lowercase().replaceFirstChar { char -> char.titlecase() }
    }