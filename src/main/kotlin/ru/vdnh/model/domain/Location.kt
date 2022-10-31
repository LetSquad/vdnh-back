package ru.vdnh.model.domain

data class Location(
    val id: Long,
    val priority: Int?,
    val type: LocationType,
    val coordinatesId: Long,
)
