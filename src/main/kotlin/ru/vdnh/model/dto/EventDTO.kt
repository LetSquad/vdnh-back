package ru.vdnh.model.dto

import java.math.BigInteger

data class EventDTO(
    val id: BigInteger,
    val title: String?,
    val titleEn: String?,
    val titleCn: String?,
    val priority: Int?,
    val url: String?,
    val imageUrl: String?,
    val coordinatesId: BigInteger,
    val categoryCode: String?,
    val typeCode: String?,
    val subjectCode: String?
)