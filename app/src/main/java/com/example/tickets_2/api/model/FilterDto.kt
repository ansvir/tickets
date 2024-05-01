package com.example.tickets_2.api.model

import com.example.tickets_2.models.api.KvitkiEventType
import java.math.BigDecimal
import java.util.Date

/**
 * Содержит данные фильтра для фрагмента [com.example.tickets_2.fragment.FilterFragment]
 */
data class FilterDto(var date: Date, var fromPrice: BigDecimal, var toPrice: BigDecimal,
                     var eventType: KvitkiEventType)
