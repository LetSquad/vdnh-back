package ru.vdnh.model.entity

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigInteger

@Schema(description = "Event model")
@Table("event")
data class EventEntity(
    @Id val id: BigInteger,
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