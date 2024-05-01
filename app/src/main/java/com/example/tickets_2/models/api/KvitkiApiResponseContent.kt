package com.example.tickets_2.models.api

/**
 * Сущность-содержание ответа от API kvitki.by
 */
data class KvitkiApiResponseContent(
    val event: List<KvitkiEventApiResponse>,
    val listInfo: KvitkiTotalApiResponse,
)