package com.codemakers.aquaplus.ui.extensions

import java.time.LocalDate

val LocalDate.isAfterToday: Boolean
    get() {
        val today = LocalDate.now()
        return this.year != today.year ||
                (this.year == today.year && this.monthValue != today.monthValue) ||
                (this.year == today.year && this.monthValue == today.monthValue && this.dayOfMonth != today.dayOfMonth)
    }

val LocalDate.isAfterToday2: Boolean
    get() {
        val today = LocalDate.now()
        return this.year != today.year ||
                (this.year == today.year && this.monthValue != today.monthValue) ||
                (this.year == today.year && this.monthValue == today.monthValue && this.dayOfMonth != today.dayOfMonth)
    }