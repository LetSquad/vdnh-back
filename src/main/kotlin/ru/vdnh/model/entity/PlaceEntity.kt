package ru.vdnh.model.entity

import java.sql.Timestamp

data class PlaceEntity(
    val id: Long,
    val title: String,
    val titleEn: String?,
    val titleCn: String?,
    val priority: Int?,
    val url: String,
    val imageUrl: String?,
    val ticketsUrl: String?,
    val isActive: Boolean,

    val coordinatesId: Long,
    val scheduleId: Long?,

    val typeCode: String,
    val subjectCode: String?,

    val createdAt: Timestamp
)
