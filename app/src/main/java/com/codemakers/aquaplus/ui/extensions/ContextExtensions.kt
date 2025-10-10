package com.codemakers.aquaplus.ui.extensions

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.print.PrintHelper
import com.codemakers.aquaplus.R

// Notifications
const val NOTIFICATION_CHANNEL_NAME = "Sync AquaPlus"
const val NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts"
const val CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
const val NOTIFICATION_ID = 1

fun Context.removeStatusNotification() {
    NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID)
}

fun Context.showNotification(
    title: String,
    messageBody: String,
    pendingIntent: PendingIntent? = null,
    isPriority: Boolean? = false,
) {
    val name = NOTIFICATION_CHANNEL_NAME
    val description = NOTIFICATION_CHANNEL_DESCRIPTION
    val importance =
        if (isPriority == true) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_LOW
    val channel = NotificationChannel(CHANNEL_ID, name, importance)
    channel.description = description
    val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
        setSmallIcon(R.mipmap.ic_launcher)
        setContentTitle(title)
        setContentText(messageBody)
        setContentIntent(pendingIntent)
        setGroup(this@showNotification.packageName)
        setAutoCancel(true)
        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        setPriority(if (isPriority == true) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_LOW)
    }
    NotificationManagerCompat.from(this).apply {
        createNotificationChannel(channel)
        notify(System.currentTimeMillis().toInt(), builder.build())
    }
}

fun Context.doPrint(bitmap: Bitmap) {
    PrintHelper(this).apply {
        scaleMode = PrintHelper.SCALE_MODE_FIT
    }.also { printHelper ->
        printHelper.printBitmap("droids.jpg - test print", bitmap)
    }
}