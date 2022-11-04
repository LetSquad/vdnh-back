package ru.vdnh.model.dto

import java.math.BigDecimal

// TODO merge with GeometryDTO
data class GeometryRouteDTO(
    val type: String,
    val coordinates: List<List<BigDecimal>>
)
