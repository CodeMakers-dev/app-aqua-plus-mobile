package com.codemakers.aquaplus.ui.extensions

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import kotlin.math.roundToInt

fun Double.roundTo2Decimals(): Double =
    (this * 100.0).roundToInt() / 100.0

fun Double.cop(): String {
    val nf = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    nf.maximumFractionDigits = 2
    nf.minimumFractionDigits = 2
    nf.currency = Currency.getInstance("COP")
    return nf.format(this)
}