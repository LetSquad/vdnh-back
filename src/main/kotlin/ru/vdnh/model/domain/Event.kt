package ru.vdnh.model.domain

data class Event(
    val id: Long,
    val title: String?,
    val titleEn: String?,
    val titleCn: String?,
    val priority: Int?,
    val url: String?,
    val imageUrl: String?,
    val coordinatesId: Long?,
    val typeCode: String?,
    val subjectCode: String?
)