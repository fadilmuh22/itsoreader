package com.diskominfo.itsoreader.data


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.diskominfo.itsoreader.R
import com.diskominfo.itsoreader.ui.NewDataActivity
import com.diskominfo.itsoreader.data.model.EktpReadModel
import com.diskominfo.itsoreader.data.remote.SocketManager
import com.diskominfo.itsoreader.ui.MainActivity
import com.google.gson.Gson

class SocketService : Service() {
    companion object {
        private const val TAG = "SocketService"
        private const val CHANNEL_ID = "SocketServiceChannel"
        private const val NOTIFICATION_ID = 1

        /**
         * Starts the SocketService.
         */
        fun startService(context: Context) {
            val intent = Intent(context, SocketService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        /**
         * Stops the SocketService.
         */
        fun stopService(context: Context) {
            val intent = Intent(context, SocketService::class.java)
            context.stopService(intent)
        }
    }

    private lateinit var socketManager: SocketManager

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val notification = createNotification("Socket Service is running")
        startForeground(NOTIFICATION_ID, notification)

        socketManager = SocketManager()
        socketManager.connect()
        socketManager.onNewDataReceived { ektpRead ->
            handleNewData(ektpRead)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // If you need to handle start commands, do it here
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        socketManager.disconnect()
        Log.d(TAG, "SocketService destroyed and disconnected.")
    }

    override fun onBind(intent: Intent?): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    /**
     * Handles incoming Welcome data by showing a notification and opening a new screen.
     */
    private fun handleNewData(welcome: EktpReadModel) {
        // Show a notification for the new data
        showNewDataNotification(welcome)
    }

    /**
     * Creates the notification channel for the foreground service and notifications.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Socket Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    /**
     * Creates the foreground service notification.
     */
    private fun createNotification(contentText: String): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Socket Service")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.baseline_add_card_24) // Replace with your app's icon
            .setContentIntent(pendingIntent)
            .build()
    }

    /**
     * Shows a notification when new data is received.
     */
    private fun showNewDataNotification(welcome: EktpReadModel) {
        val notificationIntent = Intent(this, NewDataActivity::class.java).apply {
            putExtra("welcomeData", Gson().toJson(welcome))
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("New Data Received")
            .setContentText("Tap to view new data from ${welcome.namaLengkap}")
            .setSmallIcon(R.drawable.baseline_add_card_24) // Replace with your app's icon
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(welcome.hashCode(), notification)

        try {
            Log.d(TAG, "Showing notification for new data: $welcome")
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(notificationIntent)
        } catch (e: Exception) {
            Log.e(TAG, "Error starting activity: ${e.message}")
        }
    }
}
