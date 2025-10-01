package com.codemakers.aquaplus.ui.extensions

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun Long.cop(): String {
    val nf = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    nf.maximumFractionDigits = 2
    nf.minimumFractionDigits = 2
    nf.currency = Currency.getInstance("COP")
    return nf.format(this)
}

fun Double.cop(): String {
    val nf = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    nf.maximumFractionDigits = 2
    nf.minimumFractionDigits = 2
    nf.currency = Currency.getInstance("COP")
    return nf.format(this)
}