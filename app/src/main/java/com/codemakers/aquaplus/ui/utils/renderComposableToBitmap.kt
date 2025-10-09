package com.codemakers.aquaplus.ui.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.print.PrintHelper
import java.io.File

fun doPrint(context: Context, bitmap: Bitmap) {
    PrintHelper(context).apply {
        scaleMode = PrintHelper.SCALE_MODE_FIT
    }.also { printHelper ->
        printHelper.printBitmap("droids.jpg - test print", bitmap)
    }
}

fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }
}


