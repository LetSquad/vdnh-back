package ru.vdnh.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "Route model")
data class RouteDTO(val latitude: BigDecimal, val longitude: BigDecimal)