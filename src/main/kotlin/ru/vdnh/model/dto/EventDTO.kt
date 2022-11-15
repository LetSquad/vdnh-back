package ru.vdnh.model.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Event model")
data class EventDTO(
    val id: Long,
    val type: String,
    val geometry: GeometryDTO,
    val properties: LocationPropertiesDTO,
    val visitTime: Long?
)
