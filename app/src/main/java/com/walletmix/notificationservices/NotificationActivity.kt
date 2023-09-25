package com.walletmix.notificationservices

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.walletmix.notificationservices.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {


    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra(Constants.notificationTitle)
        val body = intent.getStringExtra(Constants.notificationBody)

        binding.apply {
            tvTitle.text = title
            tvBody.text = body
        }
    }
}