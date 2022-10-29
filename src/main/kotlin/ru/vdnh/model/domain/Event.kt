package ru.vdnh.model.domain

import ru.vdnh.model.enums.LocationPlacement
import ru.vdnh.model.enums.PaymentConditions
import java.sql.Date
import java.time.Duration
import java.time.Instant

data class Event(
    val id: Long,
    val title: String,
    val titleEn: String?,
    val titleCn: String?,
    val priority: Int,
    val visitTime: Duration,
    val placement: LocationPlacement,
    val paymentConditions: PaymentConditions,
    val url: String,
    val imageUrl: String?,
    val isActive: Boolean,
    val startDate: Date?,
    val finishDate: Date?,

    val coordinates: Coordinates?,
    val type: LocationType,
    val schedule: Schedule?,

    val typeCode: String?,
    val subjectCode: String?,

    val createdAt: Instant,
    val places: List<Long>?
)
