package ru.vdnh.model.domain

import java.math.BigDecimal

data class LocationCoordinates(
    val id: Long,
    val latitude: BigDecimal?,
    val longitude: BigDecimal?
)
