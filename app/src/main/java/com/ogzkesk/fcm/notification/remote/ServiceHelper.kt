package com.ogzkesk.fcm.notification.remote

import com.ogzkesk.fcm.notification.util.Logger
import com.ogzkesk.fcm.notification.model.NotificationMessage
import com.ogzkesk.fcm.notification.model.PostResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class ServiceHelper(private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    companion object {
        private const val BASE_URL = "https://fcm.googleapis.com/v1/projects/"
        private const val PROJECT_ID = "testfcm-7b43d"
    }

    private var fcmService: FcmService? = null

    fun createService() = apply {
        val client = OkHttpClient.Builder()
            .addInterceptor(MInterceptor())
            .build()

        fcmService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

    suspend fun postNotification(notification: NotificationMessage): Result<PostResponse> {
        return withContext(coroutineDispatcher){
            try {
                if(fcmService == null){
                    return@withContext Result.failure(Throwable("fcmService is null"))
                }
                val response = fcmService!!.postNotification(PROJECT_ID, notification)
                Logger.log(response)
                Result.success(response)
            } catch (e: HttpException) {
                Logger.log(e.code().toString() + e.message())
                Result.failure(e)
            } catch (e: Exception) {
                Logger.log(e)
                Result.failure(e)
            }
        }
    }
}