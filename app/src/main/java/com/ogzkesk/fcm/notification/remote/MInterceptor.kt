package com.ogzkesk.fcm.notification.remote

import com.ogzkesk.fcm.notification.util.Logger
import com.ogzkesk.fcm.notification.util.PreferencesManager
import okhttp3.Interceptor
import okhttp3.Response

class MInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${PreferencesManager.accessToken}")
            .addHeader("Content-Type", "application/json; UTF-8")
            .build()

        Logger.log("Headers: " + request.headers)
        return chain.proceed(request)
    }
}