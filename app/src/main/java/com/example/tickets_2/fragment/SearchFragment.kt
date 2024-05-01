package com.example.tickets_2.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
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
import com.example.tickets_2.models.api.KvitkiApiResponse
import com.example.tickets_2.models.api.KvitkiEventApiResponse
import com.example.tickets_2.service.NotificationService
import com.example.tickets_2.util.CommonUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val defaultCellPad: Int = 8
    private val cellsPerRow: Int = 3

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
    private fun populateGrid(layout: TableLayout, events: KvitkiApiResponse) {
        for (rowIndex in 0 until events.responseData.event.size step cellsPerRow) {
            val tableRow = TableRow(layout.context)
            for (cellIndex in rowIndex until (rowIndex + cellsPerRow).coerceAtMost(events.responseData.event.size)) {
                val nextEvent = events.responseData.event[cellIndex]
                val textView = TextView(tableRow.context)
                textView.text = buildTitle(nextEvent)
                textView.setPadding(defaultCellPad, defaultCellPad, defaultCellPad, defaultCellPad)
                tableRow.addView(textView)
            }
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
                    populateGrid(eventsList, response)
                } else {
                    Log.w(KvitkiRestClient.KVITKI_RESPONSE_TAG, "server returned null response")
                    notificationService.showNotification("Возникла ошибка получения мероприятий!")
                }
            }
        } else {
            notificationService.showNotification("Укажите в фильтре цену 'от' меньше или равной цене 'до'!")
            Log.d(CommonUtil.DEBUG_TAG, "Price in filter to is more than price from!")
        }

    }

}