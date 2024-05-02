package com.example.tickets_2.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.tickets_2.R
import com.example.tickets_2.models.common.NotificationDto
import com.example.tickets_2.storage.NotificationSharedPreferences
import com.example.tickets_2.util.CommonUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private val defaultCellPad: Int = 8
    private val defaultNotificationTitle = "Уведомление о мероприятии"

    @Inject
    lateinit var notificationSharedPreferences: NotificationSharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notification, container, false)
        val allNotifications = view.findViewById<TableLayout>(R.id.allNotifications)
        populateGrid(allNotifications, notificationSharedPreferences.findAllNotifications())
        val addNotification = view.findViewById<FloatingActionButton>(R.id.addNotification)
        addNotification.setOnClickListener {
            var nextId = notificationSharedPreferences.findAllNotifications().lastOrNull()?.id
            if (nextId == null) {
                nextId = 1L
            } else {
                nextId++
            }
            val newNotification = NotificationDto.create(nextId, defaultNotificationTitle, FilterFragment.filter)
            notificationSharedPreferences.storeNotification(newNotification)
            populateGrid(allNotifications, notificationSharedPreferences.findAllNotifications())
        }
        return view
    }

    // заполняет таблицу NotificationFragment уведомлениями
    @RequiresApi(Build.VERSION_CODES.O)
    private fun populateGrid(layout: TableLayout, notifications: List<NotificationDto>) {
        for (nextNotification in notifications) {
            val tableRow = TableRow(layout.context)
            val textView = TextView(tableRow.context)
            textView.text = buildTitle(nextNotification)
            textView.setPadding(defaultCellPad, defaultCellPad, defaultCellPad, defaultCellPad)
            tableRow.addView(textView)
            layout.addView(tableRow)
        }
    }

    // Получает описание уведмоления в виде String
    @RequiresApi(Build.VERSION_CODES.O)
    private fun buildTitle(notification: NotificationDto): String {
        return notification.id.toString() + ". " + notification.message + "\n" +
                CommonUtil.dateToString(notification.filter.date) + "\n" + notification.filter.fromPrice +
                " - " + notification.filter.toPrice + " BYN\n" + notification.filter.eventType.value

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment NotificationFragment.
         */
        @JvmStatic
        fun newInstance() = NotificationFragment()
    }
}