package ru.vdnh.model.domain

import ru.vdnh.model.enums.CategoryType
import ru.vdnh.model.enums.LocationPlacement
import ru.vdnh.model.enums.PaymentConditions
import ru.vdnh.model.enums.VisitorNavigationType
import java.time.Duration

data class Location(
    val coordinates: Coordinates, // TODO у всех есть координаты или нет?
    val locationCodeType: CategoryType,

    val schedule: Schedule?, // TODO сделать фильтрацию на основе расписания
    val visitorType: VisitorNavigationType?,
    val visitTime: Duration?,
    val placement: LocationPlacement?,
    val paymentConditions: PaymentConditions?,
    val priority: Int?,
)
