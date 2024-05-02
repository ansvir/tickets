package com.example.tickets_2.models.common

/**
 * Содержит данные уведомления для пользователя
 */
data class NotificationDto(val id: Long, val message: String, val filter: FilterDto) {

    companion object {

        /**
         * Метод-фабрика для [NotificationDto]
         */
        fun create(id: Long, message: String, filter: FilterDto): NotificationDto {
            return NotificationDto(id, message, filter)
        }

    }

}
