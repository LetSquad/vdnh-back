package ru.vdnh.model.dto


data class PreparedRouteDataDTO(
    val name: String,
    val description: String,
    val previewImageUrl: String,
    val imageUrl: String,
    val mapData: RouteDTO
)
