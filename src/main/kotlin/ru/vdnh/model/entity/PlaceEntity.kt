package ru.vdnh.model.entity

import ru.vdnh.model.enums.LocationPlacement
import ru.vdnh.model.enums.PaymentConditions
import java.sql.Timestamp

data class PlaceEntity(
    val id: Long,
    val title: String,
    val titleEn: String?,
    val titleCn: String?,
    val priority: Int,
    val visitTimeMinutes: Int,
    val placement: LocationPlacement,
    val paymentConditions: PaymentConditions,
    val url: String,
    val imageUrl: String?,
    val ticketsUrl: String?,
    val isActive: Boolean,

    val coordinates: CoordinatesEntity,
    val type: LocationTypeEntity,
    val schedule: ScheduleEntity?,

    val typeCode: String,
    val subjectCode: String?,

    val createdAt: Timestamp
)
