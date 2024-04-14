package com.example.tickets_2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import com.google.gson.Gson
import android.widget.ArrayAdapter
import android.widget.Spinner
//import com.example.tickets_2.EventListResponse



class MainActivity : AppCompatActivity() {

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchDataFromServer()

    }

    private fun fetchDataFromServer() {
        // Создаем клиент OkHttp
        val client = OkHttpClient()

        // Создаем запрос
        val request = Request.Builder()
            .url("https://www.kvitki.by/ajaxCaller/method:getConcertsListInfo/id:1088/type:catalog_category/status:insales/date:19.04.2024/order:date,asc/page:1/design:kvitki/portal:2/?fields%5Bevent%5D=id,title,promoterId,minPrice,decoratedTitle,discount,shortUrl,shortImageUrl,specialStatus,venueDescription,salesStatus,localisedStartDate,localisedEndDate,startTime,endTime,centerId,prices,buyButtonConfig,badgeData,type,customTargetUrl,subMode,decoratedShortContent,salestart,countryCode")
            .build()

        // Запускаем запрос асинхронно с использованием корутин
        GlobalScope.launch(Dispatchers.IO) {
            // Отправляем запрос и получаем ответ
            val response = client.newCall(request).execute()

            // Обрабатываем ответ в основном потоке
            launch(Dispatchers.Main) {
                handleResponse(response)
            }
        }
    }
    private fun handleResponse(response: Response) {
        // Проверяем, успешен ли запрос
        if (response.isSuccessful) {
            // Получаем тело ответа
            val responseBody = response.body?.string()
            if (responseBody != null) {
                Log.d("Response Body", responseBody)
                try {
                    // Парсим JSON-ответ в объект EventListResponse
                    val eventListResponse = Gson().fromJson(responseBody, ResponseData::class.java)
                    // Создаем экземпляр Gson


                    // Показываем в логах, что было распарсено из JSON
                    Log.d("ResponseData", eventListResponse.toString())

                    // Получаем список событий
                    val events = eventListResponse.responseData.event

                    // Получаем ссылку на спиннер по его ID
                    val spinner: Spinner = findViewById(R.id.concert_spinner)

                    // Проверяем, что список событий не равен null и не пуст
                    if (!events.isNullOrEmpty()) {
                        // Выводим список названий концертов
                        val concertNames = events.map { it.title }
                        val adapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_item, concertNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter // Устанавливаем адаптер для спиннера

                        // Выводим названия событий в лог
                        events.forEach { event ->
                            Log.d("Event Title", event.title)
                            // Здесь можно добавить вывод других свойств события, если нужно
                        }
                    } else {
                        // Список событий пустой
                        Log.d("No concerts available", "")
                    }
                } catch (e: Exception) {
                    // Обработка ошибок парсинга JSON
                    Log.e("JSON parsing error", e.message ?: "Unknown error")
                }
            } else {
                // Обработка случая, когда тело ответа пустое
                Log.d("Empty response body", "")
            }
        } else {
            // Если запрос не успешен, отображаем уведомление об ошибке
            showNotification("Error")
        }
    }


    private fun showNotification(message: String) {
        // Показываем уведомление вашему пользователю
        println(message)
    }
}


//    private fun handleResponse(response: Response) {
//        // Проверяем, успешен ли запрос
//        if (response.isSuccessful) {
//            // Если запрос успешен, парсим JSON и выводим список концертов
//            val responseBody = response.body?.string()
//            if (responseBody != null) {
//                Log.d("Response Body", responseBody)
//                val eventListResponse = Gson().fromJson(responseBody, ResponseData::class.java)
//                // Показываем в логах, что было распарсено из JSON
//                Log.d("ResponseData", eventListResponse.toString())
//
//
//                val events = eventListResponse.events
//                val spinner: Spinner = findViewById(R.id.concert_spinner)
//
//
//                if (eventListResponse != null && eventListResponse.events != null) { // Проверяем, что объекты не равны null
//                    // Проверяем, есть ли концерты в списке
//                    if (eventListResponse.events.isNotEmpty()) {
//
//                        // Выводим список названий концертов
//                        val concertNames = events.map { it.title }
//                        val adapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_spinner_item, concertNames)
//                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                        spinner.adapter = adapter // Устанавливаем адаптер для спиннера
//
//                        events.forEach { event ->
//                            println("Event Title: ${event.title}")
//                            // Выводите другие свойства события по аналогии
//                        }
//                    } else {
//                        println("No concerts available")
//                    }
//                } else {
//                    // Обработка случая, когда объект eventListResponse или список концертов равны null
//                    println("eventListResponse or events list is null")
//
//                }
//            } else {
//                // Обработка случая, когда тело ответа пустое
//                println("Empty response body")
//            }
//        } else {
//            // Если запрос не успешен, отображаем уведомление об ошибке
//            showNotification("Error")
//        }
//    }








//
//
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//
//       // ticketLinkEditText = findViewById(R.id.ticket_link_edittext)
//        //нужно распарсить в инт
//        var costMin = ParcelField(R.id.minCost)
//        var costMax = ParcelField(R.id.maxCost)
//        submitButton = findViewById()
//
//        // Set click listener for the submit button
//        submitButton.setOnClickListener {
//           // val ticketLink = ticketLinkEditText.text.toString()
//
//
//
//            // Parse ticket link and extract ticket price (later)
//
//            var ticketPrice = Random.nextInt(0, 100)
//
//
//
//            // Compare ticket price with user's desired cost range
//            if (ticketPrice > costMin && ticketPrice < costMax) {
//                // Trigger alert to notify user
//                // You can implement this part using notifications or dialog boxes
//                // For simplicity, you can just display a toast message
//                //showToast("Ticket price is within the desired range!")
//            }
//        }
//    }
//
//    // Function to parse ticket price from the provided link
////    private fun parseTicketPrice(ticketLink: String): Double {
//        // Implement parsing logic here
//        // Return the extracted ticket price}
//
//
//}


