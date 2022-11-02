package ru.vdnh.model.dto

import java.time.LocalDateTime

data class DateNavigationDTO(
    val dateStart: LocalDateTime?,
    val dateFinish: LocalDateTime?,
)
