package ru.vdnh.model.domain

import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalTime

data class Coordinates(
    val id: Long,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
    val connections: List<Long>,
    val loadFactor: Map<DayOfWeek, Map<LocalTime, Double>>
)
