package com.example.tickets_2.models.common

import com.example.tickets_2.models.api.KvitkiEventType
import java.math.BigDecimal
import java.util.Calendar
import java.util.Date

/**
 * Содержит данные фильтра для фрагмента [com.example.tickets_2.fragment.FilterFragment]
 */
data class FilterDto(var date: Date, var fromPrice: BigDecimal, var toPrice: BigDecimal,
                     var eventType: KvitkiEventType) {
    companion object {
        fun createDefault(): FilterDto {
            return FilterDto(Date(Calendar.getInstance().timeInMillis), BigDecimal(0.0),
                BigDecimal(10_000.0), KvitkiEventType.THEATER)
        }
    }
}
