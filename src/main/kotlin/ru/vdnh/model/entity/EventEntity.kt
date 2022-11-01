package ru.vdnh.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import ru.vdnh.model.enums.LocationPlacement
import ru.vdnh.model.enums.PaymentConditions
import java.sql.Date
import java.sql.Timestamp

@Table("event")
data class EventEntity(
    @Id val id: Long,
    val title: String,
    val titleEn: String?,
    val titleCn: String?,
    val priority: Int,
    val visitTimeMinutes: Int,
    val placement: LocationPlacement,
    val paymentConditions: PaymentConditions,
    val url: String,
    val imageUrl: String?,
    val isActive: Boolean,
    val startDate: Date?,
    val finishDate: Date?,

    val coordinates: CoordinatesEntity?,
    val type: LocationTypeEntity,
    val schedule: ScheduleEntity?,

    val typeCode: String,
    val subjectCode: String?,

    val createdAt: Timestamp
)
