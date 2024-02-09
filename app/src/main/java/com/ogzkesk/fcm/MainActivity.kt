package com.ogzkesk.fcm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.ogzkesk.fcm.home.Home
import com.ogzkesk.fcm.notification.PushNotification
import com.ogzkesk.fcm.notification.model.Message
import com.ogzkesk.fcm.notification.model.Notification
import com.ogzkesk.fcm.ui.theme.FcmTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private lateinit var pushNotification: PushNotification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pushNotification = PushNotification(this, lifecycleScope)
        setContent {
            FcmTheme {
                Home(::sendRemoteMessage)
            }
        }
    }

    private fun sendRemoteMessage(remoteToken: String, notification: Notification) {
        if (remoteToken.isEmpty()) {
            showToast("Remote token is empty")
            return
        }
        lifecycleScope.launch {
            val message = Message(notification, remoteToken, null, null)
            pushNotification.postNotification(message).collect { result ->
                if (result.isSuccess) {
                    showToast("notification sent ${result.getOrNull()}")
                }
            }
        }
    }
}



