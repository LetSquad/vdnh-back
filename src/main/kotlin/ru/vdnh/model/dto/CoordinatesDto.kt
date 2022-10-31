package ru.vdnh.model.dto

import java.math.BigDecimal

data class CoordinatesDto(
    val id: Long,
    val latitude: BigDecimal,
    val longitude: BigDecimal
)
