package com.ogzkesk.fcm.notification.remote

import com.ogzkesk.fcm.notification.model.NotificationMessage
import com.ogzkesk.fcm.notification.model.PostResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface FcmService {

    @POST("{projectId}/messages:send")
    suspend fun postNotification(
        @Path("projectId") projectId: String,
        @Body notification: NotificationMessage,
    ) : PostResponse

}