package com.example.cinemagic.NotificationScreen.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val service: NotificationService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var currentAuthorization:String = ""
    private var currentNotifications: NotificationSearchResults? = null
    suspend fun loadNotificationResults(authorization:String): Result<NotificationSearchResults?> {


        return run {
            currentAuthorization = authorization
            withContext(ioDispatcher) {
                try {
                    val response = service.notificationSearch(auth = authorization)
                    if (response.isSuccessful) {
                        currentNotifications = response.body()
                        Result.success(currentNotifications)
                    } else {
                        Result.failure(Exception(response.errorBody()?.string()))
                    }
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
        }
    }
}