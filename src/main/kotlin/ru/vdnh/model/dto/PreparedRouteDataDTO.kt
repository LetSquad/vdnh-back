package ru.vdnh.model.dto


data class PreparedRouteDataDTO(
    val id: Long,
    val title: Map<String, String?>, // TODO: локализация
    val description: Map<String, String?>, // TODO: локализация
    val imageUrl: String,
    val route: RouteDTO
)
