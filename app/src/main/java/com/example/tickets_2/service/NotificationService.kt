package com.example.tickets_2.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.tickets_2.TicketsApplication
import com.example.tickets_2.models.common.NotificationDto
import dagger.hilt.android.AndroidEntryPoint

/**
 * Сервис для отправки уведомлений пользователю
 */
@AndroidEntryPoint
class NotificationService: Service() {

    companion object {
        const val DEFAULT_CHANNEL_ID = "default_channel_id"
        const val DEFAULT_CHANNEL_NAME = "Агрегатор билетов | Уведомление"
    }

    fun sendNotification(notification: NotificationDto) {
        val notificationManager =
            TicketsApplication.instance.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // создаём канал уведомлений для андроида версии больше O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // создаём само уведомление
        val builder = NotificationCompat.Builder(TicketsApplication.instance, DEFAULT_CHANNEL_ID)
            .setSmallIcon(androidx.core.R.drawable.notification_icon_background)
            .setContentTitle(DEFAULT_CHANNEL_NAME)
            .setContentText(notification.message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        // показываем уведомление
        notificationManager.notify(notification.id.toInt(), builder.build())
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}