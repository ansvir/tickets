package com.example.tickets_2.storage

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.example.tickets_2.TicketsApplication
import com.example.tickets_2.models.common.NotificationDto
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

/**
 * Сервис для работы с хранением уведомлением
 */
@AndroidEntryPoint
class NotificationSharedPreferences: Service() {

    companion object {
        const val NOTIFICATIONS_KEY = "NOTIFICATIONS"
        val NOTIFICATION_PREFERENCES = TicketsApplication.instance.getSharedPreferences(NOTIFICATIONS_KEY, Context.MODE_PRIVATE)
    }

    fun storeNotification(notification: NotificationDto) {
        val editor = NOTIFICATION_PREFERENCES.edit()
        editor.putString(notification.id.toString(), Gson().toJson(notification))
        editor.apply()
    }

    fun findAllNotifications(): List<NotificationDto> {
        return NOTIFICATION_PREFERENCES.all.mapValues { Gson().fromJson(it.value.toString(),
            NotificationDto::class.java) }.values.toMutableList()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}