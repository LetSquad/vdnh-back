package ru.vdnh.model.domain

import java.math.BigInteger

data class Event(
    val id: BigInteger,
    val title: String?,
    val titleEn: String?,
    val titleCn: String?,
    val priority: Int?,
    val url: String?,
    val imageUrl: String?,
    val coordinatesId: BigInteger,
    val typeCode: String?,
    val subjectCode: String?
)