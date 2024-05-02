package com.example.tickets_2.activity

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.tickets_2.R
import com.example.tickets_2.api.kvitki.KvitkiRestClient
import com.example.tickets_2.fragment.FilterFragment
import com.example.tickets_2.fragment.NotificationFragment
import com.example.tickets_2.fragment.SearchFragment
import com.example.tickets_2.service.NotificationService
import com.example.tickets_2.storage.NotificationSharedPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
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
                                if (response.responseData.event != null) {
                                    if (response.responseData.event!!.isNotEmpty()) {
                                        notificationService.sendNotification(it)
                                        // добавить когда будут работать уведомления
                                        // notificationSharedPreferences.deleteById(it.id)
                                    }
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


