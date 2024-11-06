package com.example.itsotest.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.itsotest.R
import com.example.itsotest.data.ResultState
import com.example.itsotest.data.UserRepository
import com.example.itsotest.di.Injection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class RetryService : Service() {

    private lateinit var repository: UserRepository
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        repository = Injection.provideRepository(this)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            val tamuData = repository.getSession()
            if (tamuData != null) {
                // If uploadTamuDelay is a suspend function, use it directly
                repository.uploadTamuDelay(tamuData).observeForever { result ->
                    when (result) {
                        is ResultState.Loading-> {

                        }

                        is ResultState.Success -> { // Handle success
                            showNotification("Data berhasil dikirim")
                            stopSelf()
                        }
                        is ResultState.Error -> { // Handle error
                            // You can log or notify about the error
                            Log.e("RetryService", "Error uploading data: ${result.error}")
                        }
                    }
                }
            }
        }
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Retry Channel"
            val descriptionText = "Channel untuk notifikasi pengiriman ulang"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("retry_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, "retry_channel")
            .setContentTitle("Pengiriman Ulang")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification) // Consider using unique IDs
    }

    override fun onBind(intent: Intent): IBinder? = null
}
