package com.ogzkesk.fcm.notification.model


import com.google.gson.annotations.SerializedName

data class NotificationMessage(
    @SerializedName("message") val message: Message?
)