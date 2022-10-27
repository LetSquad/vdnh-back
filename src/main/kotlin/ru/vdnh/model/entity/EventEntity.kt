package ru.vdnh.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigInteger

@Table("event")
data class EventEntity(
    @Id val id: BigInteger,
    val title: String,
    val titleEn: String?,
    val titleCn: String?,
    val priority: Int?,
    val url: String?,
    val imageUrl: String?,
    val coordinatesId: BigInteger,
    val typeCode: String,
    val subjectCode: String?
)