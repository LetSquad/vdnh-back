package ru.vdnh.model.entity

data class RouteEntity(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val previewImageUrl: String
)
