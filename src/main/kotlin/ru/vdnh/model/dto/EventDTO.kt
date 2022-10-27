package ru.vdnh.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigInteger

@Schema(description = "Event model")
data class EventDTO(
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