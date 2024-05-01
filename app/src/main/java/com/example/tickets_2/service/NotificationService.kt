package com.example.tickets_2.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.tickets_2.R
import com.example.tickets_2.TicketsApplication
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationService: Service() {

    fun showNotification(message: String) {
        val notificationManager = TicketsApplication.instance.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        val channelId = "default_channel_id"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Создаем канал уведомлений (требуется для API 26+)
            val channelName = "Default Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(notificationChannel)
        }
//        // Создаем интент для запуска вашей активити
//        val intent = Intent(this, MainActivity::class.java)
//        // Устанавливаем флаги для интента, чтобы он запускал вашу активити, даже если приложение находится в фоновом режиме
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        val notificationBuilder = NotificationCompat.Builder(TicketsApplication.instance, channelId)
            .setContentTitle("Уведомление")
            .setContentText(message)
            .setSmallIcon(R.drawable.baseline_add_alert_24) //  иконкa уведомления
            .setAutoCancel(true)
        //.setContentIntent(pendingIntent) // Устанавливаем интент для запуска активити по нажатию на уведомление

        val notification = notificationBuilder.build()
        notificationManager.notify(0, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}