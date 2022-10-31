package ru.vdnh.model.entity

import java.math.BigDecimal

data class CoordinatesEntity(
    val id: Long,
    val latitude: BigDecimal?,
    val longitude: BigDecimal?,
    val connections: String?,
    val loadFactor: String?
)
