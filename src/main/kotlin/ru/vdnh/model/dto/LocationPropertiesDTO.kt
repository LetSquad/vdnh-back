package ru.vdnh.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import ru.vdnh.model.enums.CategoryType
import ru.vdnh.model.enums.LocationTag

@Schema(description = "Property model")
data class LocationPropertiesDTO(
    val category: CategoryType,
    val isVisible: Boolean,
    val zoom: Double?,
    val color: String,
    val title: Map<String, String?>,
    val shortTitle: Map<String, String?>,
    val type: Map<String, String?>,
    val tag: LocationTag,
    val icon: String,
    val url: String,
    val ticketsUrl: String?,
    val pic: String?,
    val scheduleClosingTime: String?,
    val scheduleDayOff: Boolean?,
    val scheduleAdditionalInfo: List<String>?,
    val places: List<Long>?,
    val events: List<Long>?
)
