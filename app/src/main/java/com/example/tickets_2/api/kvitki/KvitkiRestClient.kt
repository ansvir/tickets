package com.example.tickets_2.api.kvitki

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.tickets_2.api.model.FilterDto
import com.example.tickets_2.models.api.KvitkiApiResponse
import com.example.tickets_2.util.CommonUtil
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * REST-клиент для API kvitki.by
 */
class KvitkiRestClient {

    companion object {
        var KVITKI_URL = "https://www.kvitki.by/ajaxCaller/"
        var KVITKI_RESPONSE_TAG = "KVITKI_RESPONSE"
    }

    /**
     * Получает список меропритий по фильтру и, при получении результата, обрабатывает ответ
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getConcertsListInfo(filter: FilterDto, onResponse: (KvitkiApiResponse?) -> Unit) {
        // Создаем клиент OkHttp
        val client = OkHttpClient()

        val eventId = filter.eventType.id
        val date = CommonUtil.dateToString(filter.date)

        // Создаем запрос
        val requestUrl = KVITKI_URL + "method:getConcertsListInfo/id:$eventId/type:catalog_category/status:insales" +
                "/date:$date/order:date,asc/page:1/design:kvitki/portal:2/?fields%5Bevent%5D=id," +
                "title,promoterId,minPrice,decoratedTitle,discount,shortUrl,shortImageUrl," +
                "specialStatus,venueDescription,salesStatus,localisedStartDate,localisedEndDate," +
                "startTime,endTime,centerId,prices,buyButtonConfig,badgeData,type,customTargetUrl," +
                "subMode,decoratedShortContent,salestart,countryCode"
        val request = Request.Builder()
            .url(requestUrl)
            .build()

        // Запускаем запрос асинхронно с использованием корутин
        GlobalScope.launch(Dispatchers.IO) {
            // Отправляем запрос и получаем ответ
            val response = client.newCall(request).execute()

            // Обрабатываем ответ в основном потоке
            launch(Dispatchers.Main) {
                // получаем ответ
                val concertListInfoResponse = parseConcertListInfo(response)
                // кладём ответ в очередь
                onResponse(concertListInfoResponse)
            }
        }

    }
    private fun parseConcertListInfo(response: Response): KvitkiApiResponse? {
        // Проверяем, успешен ли запрос
        if (response.isSuccessful) {
            // Получаем тело ответа
            val responseBody = response.body?.string()
            if (responseBody != null) {
                Log.d(KVITKI_RESPONSE_TAG, responseBody)

                // Парсим JSON-ответ в объект KvitkiApiResponse с помощью GSON
                val listInfoResponse = Gson().fromJson(responseBody, KvitkiApiResponse::class.java)

                // Показываем в логах, что было распарсено из JSON
                Log.d(KVITKI_RESPONSE_TAG, listInfoResponse.toString())

                // возвращаем ответ
                return listInfoResponse
            }
        }
        return null
    }

}