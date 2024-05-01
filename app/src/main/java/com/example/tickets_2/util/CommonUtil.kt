package com.example.tickets_2.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Класс-ютилити содержащий общие методы
 */
class CommonUtil {

    companion object {

        const val DEBUG_TAG: String = "DEBUG_TAG"
        const val DEFAULT_DATE_FORMAT = "dd.MM.yyyy"

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
         * Получает дату в формате строки "dd.MM.yyyy" из даты
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun dateToString(date: Date): String {
            val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)
            return formatter.format(localDate)
        }

        /**
         * Получает дату в формате "dd.MM.yyyy" из строки, иначе null
         */
        fun stringDateToDate(dateString: String): Date? {
            val format = SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.forLanguageTag("ru"))
            return try {
                format.parse(dateString)
            } catch (e: ParseException) {
                Log.d(DEBUG_TAG, "cannot parse string date to date: $dateString")
                null
            }
        }

    }

}