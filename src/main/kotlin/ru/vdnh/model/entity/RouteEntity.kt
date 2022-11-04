package ru.vdnh.model.entity

data class RouteEntity(
    val id: Long,
    val name: String,
    val description: String,
    val distanceMeters: Int,
    val durationMinutes: Int
)
