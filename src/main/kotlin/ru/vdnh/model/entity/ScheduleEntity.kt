package ru.vdnh.model.entity

data class ScheduleEntity(
    val id: Long,
    val monday: String?,
    val tuesday: String?,
    val wednesday: String?,
    val thursday: String?,
    val friday: String?,
    val saturday: String?,
    val sunday: String?,
    val additionalInfo: String?
)
