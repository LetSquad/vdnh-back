package ru.vdnh.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "Coordinate model")
data class CoordinatesDTO(
    val id: Long,
    val latitude: BigDecimal,
    val longitude: BigDecimal
)
