package com.ogzkesk.fcm

import com.ogzkesk.fcm.notification.PushNotificationService

class NotificationService : PushNotificationService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}

