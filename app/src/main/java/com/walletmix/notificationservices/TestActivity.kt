package com.walletmix.notificationservices

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        val title = intent.getStringExtra(Constants.notificationTitle)
        val body = intent.getStringExtra(Constants.notificationBody)

    }
}