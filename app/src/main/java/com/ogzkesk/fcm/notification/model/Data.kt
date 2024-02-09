package com.ogzkesk.fcm.notification.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("story_id") val storyId: String?
)