package ru.vdnh.model.dto

data class RouteDTO(
    val geometry: GeometryRouteDTO,
    val mapPoints: List<MapPointDTO>
)
