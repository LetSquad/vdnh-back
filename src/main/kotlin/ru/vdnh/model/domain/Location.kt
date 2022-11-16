package ru.vdnh.model.domain

import ru.vdnh.model.enums.CategoryType
import ru.vdnh.model.enums.LocationPlacement
import ru.vdnh.model.enums.PaymentConditions
import java.time.Duration

data class Location(
    val locationId: Long,
    val coordinates: Coordinates,
    val locationCodeType: CategoryType,

    val schedule: Schedule?,
    val visitTime: Duration?,
    val placement: LocationPlacement?,
    val paymentConditions: PaymentConditions?,
    val priority: Int?,
    val subjectCode: String?,
    val typeCode: String?,
    val description: Map<String, String?>?
)
