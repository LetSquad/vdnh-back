package ru.vdnh.model.domain

import java.time.DayOfWeek
import java.time.LocalDate

data class Schedule(
    val monday: WorkingHours?,
    val tuesday: WorkingHours?,
    val wednesday: WorkingHours?,
    val thursday: WorkingHours?,
    val friday: WorkingHours?,
    val saturday: WorkingHours?,
    val sunday: WorkingHours?,
    val additionalInfo: List<String>
) {

    fun findTodayWorkingHours(): WorkingHours? = when (LocalDate.now().dayOfWeek) {
        DayOfWeek.MONDAY -> monday
        DayOfWeek.TUESDAY -> tuesday
        DayOfWeek.WEDNESDAY -> wednesday
        DayOfWeek.THURSDAY -> thursday
        DayOfWeek.FRIDAY -> friday
        DayOfWeek.SATURDAY -> saturday
        DayOfWeek.SUNDAY -> sunday
        else -> null
    }
}
