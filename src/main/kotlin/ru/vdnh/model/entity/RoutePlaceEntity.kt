package ru.vdnh.model.entity

data class RoutePlaceEntity(
    val routeId: Long,
    val placeId: Long,
    val placeOrder: Int,
    val description: String,
    val descriptionEn: String,
    val descriptionCn: String
)
