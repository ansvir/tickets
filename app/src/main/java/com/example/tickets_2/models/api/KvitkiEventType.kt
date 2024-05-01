package com.example.tickets_2.models.api

/**
 * Перечисление, содержащее типы мероприятий и их IDs для API Kvitki
 */
enum class KvitkiEventType(var id: Long, var value: String, var eventId: Int) {
    THEATER(0, "Театр", 1003),
    CIRCUS(1, "Цирк", 1088),
    MUSIC(2, "Музыка", 1002);

    companion object {

        /**
         * Получает мероприятие по имени
         */
        fun nameToEvent(name: String): KvitkiEventType {
            return entries.filter { e -> e.value == name }.component1()
        }

    }
}