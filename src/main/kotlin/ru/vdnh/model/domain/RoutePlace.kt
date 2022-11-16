package ru.vdnh.model.domain

data class RoutePlace(
    val routeId: Long,
    val placeId: Long,
    val placeOrder: Int,
    val description: String,
    val descriptionEn: String,
    val descriptionCn: String
)
