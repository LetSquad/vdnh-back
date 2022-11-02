package ru.vdnh.model.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Place model")
data class PlaceDTO(
    val id: Long,
    val type: String,
    val geometry: GeometryDTO,
    val properties: LocationPropertiesDTO
)