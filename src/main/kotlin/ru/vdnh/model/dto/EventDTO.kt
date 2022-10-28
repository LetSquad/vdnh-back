package ru.vdnh.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigInteger

@Schema(description = "Event model")
data class EventDTO(
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