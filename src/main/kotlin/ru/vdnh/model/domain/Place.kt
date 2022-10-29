package ru.vdnh.model.domain

import ru.vdnh.model.enums.LocationPlacement
import ru.vdnh.model.enums.PaymentConditions
import java.time.Duration
import java.time.Instant

data class Place(
    val id: Long,
    val title: String?,
    val titleEn: String?,
    val titleCn: String?,
    val priority: Int?,
    val visitTime: Duration,
    val placement: LocationPlacement,
    val paymentConditions: PaymentConditions,
    val url: String,
    val imageUrl: String?,
    val ticketsUrl: String?,
    val isActive: Boolean,

    val coordinates: LocationCoordinates,
    val schedule: Schedule?, // TODO

    val typeCode: String,
    val subjectCode: String?,

    val createdAt: Instant
)
