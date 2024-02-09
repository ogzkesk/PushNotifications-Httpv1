package com.ogzkesk.fcm.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ogzkesk.fcm.notification.util.Logger
import com.ogzkesk.fcm.notification.util.NotificationManager
import com.ogzkesk.fcm.notification.util.PreferencesManager

abstract class PushNotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        PreferencesManager.deviceToken = token
        Logger.log(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        NotificationManager.postNotification(this, message)
    }
}