package ru.vdnh.model.dto

data class HeatmapCoordinatesDTO(
    val type: String,
    val geometry: GeometryDTO,
    val properties: HeatmapPropertiesDTO
)
