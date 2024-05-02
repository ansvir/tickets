package com.example.tickets_2.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.tickets_2.R
import com.example.tickets_2.api.kvitki.KvitkiRestClient
import com.example.tickets_2.models.common.FilterDto
import com.example.tickets_2.models.api.KvitkiApiResponse
import com.example.tickets_2.models.api.KvitkiEventApiResponse
import com.example.tickets_2.models.common.NotificationDto
import com.example.tickets_2.service.NotificationService
import com.example.tickets_2.util.CommonUtil
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val defaultCellPad: Int = 8

    private lateinit var view: View

    @Inject
    lateinit var kvitkiRestClient: KvitkiRestClient
    @Inject
    lateinit var notificationService: NotificationService

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        this.view = view
        runSearch()
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment SearchFragment.
         */
        @JvmStatic
        fun newInstance() = SearchFragment()
    }

    // заполняет таблицу для SearchFragment
    private fun populateGrid(layout: TableLayout, events: KvitkiApiResponse, filter: FilterDto) {
        val eventsFiltered = filterEvents(events, filter)
        for (nextEvent in eventsFiltered) {
            val tableRow = TableRow(layout.context)
            val textView = TextView(tableRow.context)
            textView.text = buildTitle(nextEvent)
            textView.setPadding(defaultCellPad, defaultCellPad, defaultCellPad, defaultCellPad)
            tableRow.addView(textView)
            layout.addView(tableRow)
        }
    }

    // Получает описание мероприятия в виде String
    private fun buildTitle(event: KvitkiEventApiResponse): String {
        return "Цена: ${event.prices} BYN \nВремя начала: ${event.startTime.time}\nАдрес: ${event.venueDescription}"
    }

    // запускает поиск (запрос) по Kvitki API
    @RequiresApi(Build.VERSION_CODES.O)
    private fun runSearch() {
        val eventsList = view.findViewById<TableLayout>(R.id.searchGrid)

        // получаем значения от и до цен из фильтра
        val fromPrice = FilterFragment.filter.fromPrice
        val toPrice = FilterFragment.filter.toPrice

        // сравниваем цены и заполняем таблицу, либо показываем уведомление о заполнении фильтра правильно и выводим лог
        if (fromPrice <= toPrice) {
            // делаем запрос на API
            kvitkiRestClient.getConcertsListInfo(FilterFragment.filter) { response ->
                if (response != null) {
                    // заполняем таблицу
                    populateGrid(eventsList, response, FilterFragment.filter)
                } else {
                    Log.w(KvitkiRestClient.KVITKI_RESPONSE_TAG, "server returned null response")
                }
            }
        } else {
            Log.d(CommonUtil.DEBUG_TAG, "Price in filter to is more than price from!")
        }

    }

    // отфильтровывает мероприятия в соответствии с фильтром
    private fun filterEvents(response: KvitkiApiResponse, filter: FilterDto): List<KvitkiEventApiResponse> {
        return response.responseData.event.filter {
            val priceRange = it.prices
            if (priceRange.isNotEmpty()) {
                if (priceRange.contains(" - ")) {
                    val minMaxPrices = priceRange.trim().split(" - ")
                    BigDecimal(minMaxPrices.component1()) >= filter.fromPrice
                            && BigDecimal(minMaxPrices.component2()) <= filter.toPrice
                } else {
                    val price = priceRange.trim()
                    BigDecimal(price) >= filter.fromPrice && BigDecimal(price) <= filter.toPrice
                }
            } else {
                false
            }
        }.filter {
            val from = CommonUtil.stringDateToDate(it.localisedStartDate.split(" ").component2())
            val to = CommonUtil.stringDateToDate(it.localisedEndDate.split(" ").component2())
            if (from != null && to != null) {
                isDateInRange(filter.date, from, to)
            } else {
                false
            }
        }

    }

    private fun isDateInRange(filterDate: Date, fromDate: Date, toDate: Date): Boolean {
        val calendarFilter = Calendar.getInstance()
        calendarFilter.time = filterDate

        val calendarFrom = Calendar.getInstance()
        calendarFrom.time = fromDate

        val calendarTo = Calendar.getInstance()
        calendarTo.time = toDate

        if (calendarFilter.get(Calendar.YEAR) < calendarFrom.get(Calendar.YEAR) ||
            calendarFilter.get(Calendar.YEAR) > calendarTo.get(Calendar.YEAR)
        ) {
            return false
        }

        if (calendarFilter.get(Calendar.YEAR) == calendarFrom.get(Calendar.YEAR) &&
            calendarFilter.get(Calendar.DAY_OF_YEAR) < calendarFrom.get(Calendar.DAY_OF_YEAR)
        ) {
            return false
        }

        if (calendarFilter.get(Calendar.YEAR) == calendarTo.get(Calendar.YEAR) &&
            calendarFilter.get(Calendar.DAY_OF_YEAR) > calendarTo.get(Calendar.DAY_OF_YEAR)
        ) {
            return false
        }

        return true
    }

}