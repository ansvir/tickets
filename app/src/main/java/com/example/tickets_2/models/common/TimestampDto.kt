package com.example.tickets_2.models.common

/**
 * Сущность, которая хранит timestamp-данные.
 */
data class TimestampDto(
    val stamp: Int,
    val date: String,
    val month: String,
    val weekDay: String,
    val time: String,
    val year: String,
    val shortDate: String
)