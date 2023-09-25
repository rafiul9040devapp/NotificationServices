package com.walletmix.notificationservices

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.walletmix.notificationservices.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {


    companion object {
        private const val channelID = "com.walletmix.notificationservices"
        private const val channelDescription = "Local Notification "
    }

    private lateinit var binding: ActivityMainBinding

    //required variables for the notification
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var notificationBuilder: NotificationCompat.Builder

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initializing notification manager
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //work will be done later
        binding.btnNotification.setOnClickListener {
            onClickRequestPermission()
        }
    }


    private fun onClickRequestPermission() {

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                showNotification()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }

    }


    private fun showNotification() {

        val title = "Title"
        val body = "Details"

        //setting up the intent for the notification
        val intent: Intent = Intent(this@MainActivity, NotificationActivity::class.java)
        intent.putExtra(Constants.notificationTitle,title)
        intent.putExtra(Constants.notificationBody,body)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //notification channel is initialized
            notificationChannel = NotificationChannel(
                channelID, channelDescription, NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.apply {
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(false)
            }

            //the notification channel is created
            notificationManager.createNotificationChannel(notificationChannel)


            notificationBuilder = NotificationCompat.Builder(this@MainActivity, channelID)
                .setSmallIcon(R.drawable.notifications_active)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)


        } else {

            notificationBuilder = NotificationCompat.Builder(this@MainActivity)
                .setSmallIcon(R.drawable.notifications_active)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
        }

        val notifyID = System.currentTimeMillis().toInt()
        notificationManager.notify(notifyID, notificationBuilder.build())

    }
}


