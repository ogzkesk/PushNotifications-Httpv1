package com.ogzkesk.fcm.notification

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.ogzkesk.fcm.BuildConfig
import com.ogzkesk.fcm.notification.util.Logger
import com.ogzkesk.fcm.R
import com.ogzkesk.fcm.notification.model.Message
import com.ogzkesk.fcm.notification.model.NotificationMessage
import com.ogzkesk.fcm.notification.model.PostResponse
import com.ogzkesk.fcm.notification.remote.ServiceHelper
import com.ogzkesk.fcm.notification.util.NotificationManager
import com.ogzkesk.fcm.notification.util.PermissionManager
import com.ogzkesk.fcm.notification.util.PreferencesManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.FileNotFoundException

class PushNotification(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val serviceHelper: ServiceHelper = ServiceHelper()
) {

    init {
        Logger.setEnabled(BuildConfig.DEBUG)
        PermissionManager.checkNotificationPermission(context)
        PreferencesManager.initSharedPreferences(context.applicationContext)
        NotificationManager.createNotificationChannel(context.applicationContext)
        serviceHelper.createService()
        setAccessToken()
    }


    fun postNotification(message: Message): Flow<Result<PostResponse>> {
        return flow {
            emit(serviceHelper.postNotification(NotificationMessage(message)))
        }.catch {
            emit(Result.failure(it))
        }
    }

    fun getDeviceToken(): String? {
        return PreferencesManager.deviceToken
    }

    fun getAccessToken(): String? {
        return PreferencesManager.accessToken
    }

    private fun setAccessToken() {
        coroutineScope.launch(coroutineDispatcher) {
            try {
                val inputStream = context.resources.openRawResource(R.raw.service_account)
                val googleCredentials = GoogleCredentials.fromStream(inputStream)
                    .createScoped(listOf(SCOPES))
                googleCredentials.refresh()

                PreferencesManager.accessToken = googleCredentials.accessToken.tokenValue
            } catch (e: FileNotFoundException) {
                Logger.log(
                    "Visit page: https://firebase.google.com/docs/cloud-messaging/migrate-v1?hl=" +
                            "en&authuser=0#provide-credentials-manually for more info. After com" +
                            "leting the steps put service_account.json file into res/raw path."
                )
            } catch (e: Exception) {
                Logger.log(e)
            }
        }
    }

    companion object {
        private const val SCOPES = "https://www.googleapis.com/auth/firebase.messaging"
    }
}