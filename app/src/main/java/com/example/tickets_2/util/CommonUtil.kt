package com.example.tickets_2.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.Calendar
import java.util.Date

/**
 * Класс-ютилити содержащий общие методы
 */
class CommonUtil {

    companion object {

        const val DEBUG_TAG: String = "DEBUG_TAG"

        /**
         * Получает текущую дату в формате String
         */
        fun currentDateToString(): String {
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1 // Январь - 0
            val year = calendar.get(Calendar.YEAR)
            return "$day.$month.$year"
        }

        /**
         * Получает указанную дату в формате String.
         * Если указанная дата меньше текущей даты, возвращает текущую дату.
         */
        fun dateOrCurrentDateToString(year: Int, monthOfYear: Int, dayOfMonth: Int): String {
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            return if (year < currentYear || (year == currentYear && monthOfYear < currentMonth)
                || (year == currentYear && monthOfYear == currentMonth && dayOfMonth < currentDayOfMonth)) {
                "$currentDayOfMonth.${currentMonth + 1}.$currentYear"
            } else {
                "$dayOfMonth.${monthOfYear + 1}.$year"
            }
        }

        /**
         * Получает дату в формате "dd.MM.yyyy"
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun dateToString(date: Date): String {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            return formatter.format(date.toInstant())
        }

    }

}