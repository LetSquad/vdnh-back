package ru.vdnh.model.dto

import java.time.DayOfWeek
import java.time.LocalTime

data class HeatmapDTO(
    val day: DayOfWeek,
    val time: LocalTime,
    val heatmap: List<HeatmapCoordinatesDTO>
)
