package ru.vdnh.model.dto

import java.util.UUID

data class RouteDTO(
    val id: UUID,
    val geometry: GeometryRouteDTO,
    val mapPoints: List<MapPointDTO>,
    val mapPointTimes: List<MapPointTimeInfoDTO>,

    val distance: Double?,
    val time: Double?
)
