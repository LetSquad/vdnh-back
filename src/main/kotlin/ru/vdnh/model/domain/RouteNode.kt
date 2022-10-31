package ru.vdnh.model.domain

import java.math.BigDecimal

data class RouteNode(
    val coordinatesId: Long,
    val latitude: BigDecimal?,
    val longitude: BigDecimal?,

    val connectedCoordinatesId: List<Long>
)
