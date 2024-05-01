package com.example.tickets_2.models.api

import com.example.tickets_2.models.common.TimestampDto

/**
 * Мероприятие, полученное как ответ на запрос к API kvitki.by
 */
data class KvitkiEventApiResponse(
    val id: Int,
    val title: String,
    val promoterId: Int,
    val minPrice: Int,
    val decoratedTitle: String,
    val discount: String,
    val shortUrl: String,
    val shortImageUrl: String,
    val specialStatus: String,
    val venueDescription: String,
    val salesStatus: String,
    val localisedStartDate: String,
    val localisedEndDate: String,
    val startTime: TimestampDto,
    val endTime: TimestampDto,
    val centerId: Int,
    val prices: String,
    val badgeData: Boolean,
    val type: String,
    val customTargetUrl: String,
    val subMode: String,
    val decoratedShortContent: Any,
    val salestart: String,
    val countryCode: String
)