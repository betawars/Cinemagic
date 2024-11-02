package com.example.cinemagic.NotificationScreen.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class NotificationSearchResults (
    @Json(name = "dates") val notificationTimeFrame:TimeFrame,
    @Json(name = "results") val notificationResultList:List<NotificationRepo>
)
