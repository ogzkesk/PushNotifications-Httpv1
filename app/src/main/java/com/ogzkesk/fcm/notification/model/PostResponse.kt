package com.ogzkesk.fcm.notification.model

import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("name") val name: String
)
