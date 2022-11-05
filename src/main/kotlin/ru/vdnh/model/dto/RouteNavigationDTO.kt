package ru.vdnh.model.dto

import ru.vdnh.model.enums.LocationPlacement
import ru.vdnh.model.enums.MovementRouteType
import ru.vdnh.model.enums.PaymentRequirements
import ru.vdnh.model.enums.PopularNavigationType
import ru.vdnh.model.enums.RouteDifficultType
import java.time.OffsetDateTime

data class RouteNavigationDTO(
    // date
    val dateTimeStart: OffsetDateTime?,
    val dateTimeEnd: OffsetDateTime?,

    // navigation
    val startPlaceId: Long?,
    val finishPlaceId: Long?, // TODO

    // criteria
    val peopleNumber: VisitorCountDTO?,
    val locationPlacement: LocationPlacement?,
    val popularity: PopularNavigationType?,
    val difficulty: RouteDifficultType?,
    val payment: PaymentRequirements?,
    val movement: MovementRouteType?,
    val loadFactor: Boolean?,
    val food: Boolean?,
    val tags: List<String>?
)
