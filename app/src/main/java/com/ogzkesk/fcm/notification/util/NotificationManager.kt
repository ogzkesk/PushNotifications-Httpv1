package com.ogzkesk.fcm.notification.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.executeBlocking
import coil.request.ImageRequest
import com.google.firebase.messaging.RemoteMessage
import com.ogzkesk.fcm.MainActivity
import com.ogzkesk.fcm.R

internal object NotificationManager {

    private const val NOTIFICATION_CHANNEL_ID = "fcm_channel_id"
    private const val NOTIFICATION_CHANNEL_NAME = "Fcm Notification Channel"
    private const val NOTIFICATION_ID = 0

    fun createNotificationChannel(context: Context) {
        if (!checkApiGreaterThan26()) {
            return
        }
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }

    fun postNotification(context: Context, remoteMessage: RemoteMessage) {

        if (!PermissionManager.hasNotificationPermission(context)) {
            Logger.log("No permission for post notification")
            return
        }

        try {
            val title: String = remoteMessage.notification?.title.orEmpty()
            val description: String = remoteMessage.notification?.body.orEmpty()
            val from: String = remoteMessage.from.orEmpty()
            val senderId = remoteMessage.senderId.orEmpty()
            val imageUri: Uri = remoteMessage.notification?.imageUrl ?: Uri.EMPTY
            val largeImage: Bitmap? = getLargeImage(context,imageUri)

            Logger.log("Notification found from: $from")
            Logger.log("Notification found imageUri: $imageUri")
            Logger.log("Notification found title: $title")
            Logger.log("Notification found description: $description")
            Logger.log("Notification found senderId: $senderId")

            val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(description)
                .setLargeIcon(largeImage)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(getPendingIntent(context))
                .setAutoCancel(true)

            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification.build())
        } catch (e: Exception) {
            Logger.log(e)
        }
    }

    private fun getPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getLargeImage(context: Context, imageUri: Uri): Bitmap? {
        return try {
            val request: ImageRequest = ImageRequest.Builder(context)
                .data(imageUri)
                .allowConversionToBitmap(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .build()

            ImageLoader(context).executeBlocking(request).drawable?.toBitmap()
        } catch (e: Exception) {
            Logger.log(e.message)
            null
        }
    }

    private fun checkApiGreaterThan26(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }
}