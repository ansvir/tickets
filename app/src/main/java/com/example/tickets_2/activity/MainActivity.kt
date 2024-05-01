package com.example.tickets_2.activity

import android.animation.ObjectAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.tickets_2.R
import com.example.tickets_2.api.kvitki.KvitkiRestClient
import com.example.tickets_2.models.api.KvitkiApiResponse
import com.example.tickets_2.models.api.KvitkiEventApiResponse
import com.example.tickets_2.service.NotificationService
import com.example.tickets_2.util.CommonUtil
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var currentDate = CommonUtil.currentDateToString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Timer().scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                kvitkiRestClient.getConcertsListInfo()
//                notificationService.showNotification("Данные обновились!")
//            }
//        }, 0, 30 * 60 * 1000) // 30 минут в миллисекундах

    }

    private fun getNewDate(events: List<KvitkiEventApiResponse>): String {
        var newDate = ""
        for (event in events) {
            // Получаем дату события из JSON-ответа (примерно)
            val eventDate = event.startTime.shortDate // Предположим, что в объекте Event есть поле startDate

            // Проверяем, если дата события больше текущей даты и даты, которую уже ранее обнаружили
            if (eventDate.isNotEmpty() && eventDate > currentDate && (newDate.isEmpty() || eventDate > newDate)) {
                newDate = eventDate
            }
        }
        return newDate
    }
}


