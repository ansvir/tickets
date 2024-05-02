package com.example.tickets_2.activity

import android.animation.ObjectAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.tickets_2.R
import com.example.tickets_2.api.kvitki.KvitkiRestClient
import com.example.tickets_2.fragment.FilterFragment
import com.example.tickets_2.fragment.NotificationFragment
import com.example.tickets_2.fragment.SearchFragment
import com.example.tickets_2.models.api.KvitkiApiResponse
import com.example.tickets_2.models.api.KvitkiEventApiResponse
import com.example.tickets_2.models.api.KvitkiEventType
import com.example.tickets_2.models.common.FilterDto
import com.example.tickets_2.models.common.NotificationDto
import com.example.tickets_2.service.NotificationService
import com.example.tickets_2.storage.NotificationSharedPreferences
import com.example.tickets_2.util.CommonUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
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
import java.util.Date
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val defaultNotificationInterval = 30 * 60 * 1000L // 30 минут в миллисекундах

    @Inject
    lateinit var notificationService: NotificationService

    @Inject
    lateinit var kvitkiRestClient: KvitkiRestClient

    @Inject
    lateinit var notificationSharedPreferences: NotificationSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<BottomNavigationView>(R.id.actionsMenu)
            .setOnItemSelectedListener { getOnItemSelectedHandler(it) }
        Timer().scheduleAtFixedRate(object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                notificationSharedPreferences.findAllNotifications()
                    .forEach {
                        kvitkiRestClient.getConcertsListInfo(it.filter) { response ->
                            if (response != null) {
                                if (response.responseData.event.isNotEmpty()) {
                                    notificationService.sendNotification(it)
                                }
                            }
                        }
                    }
            }
        }, 0, defaultNotificationInterval)

    }

    // получает обработчик выбора элемента меню
    private fun getOnItemSelectedHandler(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filterItem -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.mainFragment, FilterFragment.newInstance())
                    addToBackStack(null)
                    commit()
                }
                true
            }
            R.id.notificationItem -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.mainFragment, NotificationFragment.newInstance())
                    addToBackStack(null)
                    commit()
                }
                true
            }
            R.id.searchItem -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.mainFragment, SearchFragment.newInstance())
                    addToBackStack(null)
                    commit()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


