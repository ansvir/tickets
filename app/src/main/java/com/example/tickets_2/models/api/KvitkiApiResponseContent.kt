package com.example.tickets_2.models.api

/**
 * Сущность-содержание ответа от API kvitki.by
 */
data class KvitkiApiResponseContent(
    val events: List<KvitkiEventApiResponse>,
    val listInfo: KvitkiTotalApiResponse,
)