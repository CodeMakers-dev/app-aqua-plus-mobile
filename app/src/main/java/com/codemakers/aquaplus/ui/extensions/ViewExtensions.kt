package com.codemakers.aquaplus.ui.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.core.graphics.createBitmap

fun View.getBitmap(): Bitmap {
    val bitmap: Bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)
    draw(canvas)
    return bitmap
}