package ru.vdnh.model.domain

data class Location(
    val priority: Int?,
    val type: LocationType,
    val coordinatesId: Long,
)

enum class LocationType {
    PLACE, EVENT
}
