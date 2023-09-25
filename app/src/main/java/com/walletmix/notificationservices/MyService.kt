package com.walletmix.notificationservices

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.walletmix.notificationservices.Constants.notificationBody

class MyService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        //this token need to send in the backend API
//        Toast.makeText(this, "Token: $token ", Toast.LENGTH_SHORT).show()
        Log.d("TAG", "onNewToken: $token")
    }

    companion object {
        private const val channelID = "com.walletmix.notificationservices"
        private const val channelDescription = "Cloud Notification"

    }

    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var notificationBuilder: NotificationCompat.Builder

    override fun onMessageReceived(message: RemoteMessage) {

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var title = message.notification?.title
        var body = message.notification?.body

        showNotification(title!!, body!!)

        super.onMessageReceived(message)

    }

    private fun showNotification(title: String, body: String) {
        val intent = Intent(this, TestActivity::class.java)

        intent.putExtra(Constants.notificationTitle,title)
        intent.putExtra(notificationBody,body)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                channelID,
                channelDescription,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.apply {
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(false)
            }

            notificationManager.createNotificationChannel(notificationChannel)

            notificationBuilder = NotificationCompat.Builder(this, channelID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.notifications_active)
                .setAutoCancel(true)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        this.resources,
                        R.drawable.ic_launcher_background
                    )
                )
                .setContentIntent(pendingIntent)
        } else {
            notificationBuilder = NotificationCompat.Builder(this, channelID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.notifications_active)
                .setAutoCancel(true)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        this.resources,
                        R.drawable.ic_launcher_background
                    )
                )
                .setContentIntent(pendingIntent)

        }

        var notifyID = System.currentTimeMillis().toInt()
        notificationManager.notify(notifyID, notificationBuilder.build())
    }
}