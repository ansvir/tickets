package com.example.tickets_2

import android.app.Application
import android.content.Intent
import com.example.tickets_2.activity.MainActivity
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*

@HiltAndroidApp
class TicketsApplication : Application() {

    companion object {
        lateinit var instance: TicketsApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        CoroutineScope(Dispatchers.Main).launch {
            // переключение на главный экран
            val intent = Intent(this@TicketsApplication, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
