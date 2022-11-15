package ru.vdnh.model.domain

data class PreparedRoute(
    val id: Long,

    val title: String,
    val titleEn: String?,
    val titleCn: String?,

    val description: String,
    val descriptionEn: String?,
    val descriptionCn: String?,

    val previewImageUrl: String,
    val imageUrl: String,

    val durationSeconds: Int
)
