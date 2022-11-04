package ru.vdnh.model.dto

import ru.vdnh.model.enums.MovementRouteType
import ru.vdnh.model.enums.PaymentRequirements
import ru.vdnh.model.enums.PopularNavigationType
import ru.vdnh.model.enums.RouteDifficultType
import java.time.LocalDateTime

data class RouteNavigationDTO(
    // date
    val dateTimeStart: LocalDateTime?,
    val dateTimeEnd: LocalDateTime?,

    // navigation
    val startPlaceId: Long?,
    val finishPlaceId: Long?,

    // criteria
    val peopleNumber: VisitorCountDTO?,
    val popularity: PopularNavigationType?,
    val difficulty: RouteDifficultType?,
    val payment: PaymentRequirements?,
    val movement: MovementRouteType?,
    val loadFactor: Boolean?,
    val food: Boolean?,
    val tags: List<String>?
)
