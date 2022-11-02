package ru.vdnh.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "Geometry model")
data class GeometryDTO(val type: String, val coordinates: List<BigDecimal>)
