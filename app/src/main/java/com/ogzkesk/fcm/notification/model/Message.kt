package com.ogzkesk.fcm.notification.model


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("notification") val notification: Notification?,
    @SerializedName("token") val token: String?,
    @SerializedName("topic") val topic: String?,
    @SerializedName("data") val data: Data?,
)