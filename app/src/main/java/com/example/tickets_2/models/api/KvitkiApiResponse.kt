package com.example.tickets_2.models.api

/**
 * Сущность-ответ для API kvitki.by. Содержит поле с ответом и статус ответа.
 */
data class KvitkiApiResponse(
    val responseData: KvitkiApiResponseContent,
    val responseStatus: String
)