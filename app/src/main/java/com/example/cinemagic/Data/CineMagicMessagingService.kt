package com.example.cinemagic.Data

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class CineMagicMessagingService : FirebaseMessagingService() {
    private val TAG = "1"
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        super.onMessageReceived(remoteMessage)
        remoteMessage.data.isNotEmpty().let {
            val data = remoteMessage.data
        }

        remoteMessage.notification?.let {
            val body = it.body
            val title = it.title
        }
    }
}