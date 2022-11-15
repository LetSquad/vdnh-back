package ru.vdnh.model.entity

data class RouteEntity(
    val id: Long,

    val title: String,
    val titleEn: String?,
    val titleCn: String?,

    val description: String,
    val descriptionEn: String?,
    val descriptionCn: String?,

    val imageUrl: String,
    val previewImageUrl: String,

    val durationSeconds: Int
)
