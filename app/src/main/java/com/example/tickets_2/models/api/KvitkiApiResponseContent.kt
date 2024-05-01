package com.example.tickets_2.models.api

/**
 * Сущность-содержание ответа от API kvitki.by
 */
data class KvitkiApiResponseContent(
    var event: List<KvitkiEventApiResponse>,
    val listInfo: KvitkiTotalApiResponse,
)