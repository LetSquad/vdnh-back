package ru.vdnh.model.dto

import java.math.BigDecimal

data class HeatmapCoordinatesDTO(
    val latitude: BigDecimal,
    val longitude: BigDecimal,
    val loadFactor: Double
)
