package com.evomo.powersmart.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.evomo.powersmart.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber
import kotlin.random.Random

class AnomalyNotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Timber.d("Refreshed token: $token")
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val messageToShow =
            "timestamp: ${remoteMessage.data["timestamp"]}\nanomaly: ${remoteMessage.data["anomaly"]}\nlocation: ${remoteMessage.data["location"]}"

        Timber.d("data fcm: $messageToShow")

        remoteMessage.data.forEach {
            Timber.d("fcm data iterate: ${it.key}, ${it.value}")
        }

        remoteMessage.notification?.let { message ->
            sendNotification(message, messageToShow)
        }
    }

    private fun sendNotification(message: RemoteMessage.Notification, messageToShow: String) {
        val channelId = this.getString(R.string.default_notification_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.title)
            .setContentText(messageToShow)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            this.getString(R.string.default_notification_channel_id),
            NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(channel)

        manager.notify(Random.nextInt(), notificationBuilder.build())
    }
}