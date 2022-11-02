package ru.vdnh.model.domain

data class Schedule(
    val monday: WorkingHours?,
    val tuesday: WorkingHours?,
    val wednesday: WorkingHours?,
    val thursday: WorkingHours?,
    val friday: WorkingHours?,
    val saturday: WorkingHours?,
    val sunday: WorkingHours?,
    val additionalInfo: List<String>
)
