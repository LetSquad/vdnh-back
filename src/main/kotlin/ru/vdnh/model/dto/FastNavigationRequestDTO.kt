package ru.vdnh.model.dto

import ru.vdnh.model.enums.*

data class FastNavigationRequestDTO(
    val placeNavigation: PlaceNavigationDTO?,
    val dateNavigation: DateNavigationDTO?, // TODO not working

    val visitorType: VisitorNavigationType?,
    val routeSpeedType: RouteSpeedType?,
    val popularType: PopularNavigationType?,
    val placement: LocationPlacement?,
    val paymentConditions: PaymentConditions?,
    val withLoadFactor: Boolean?,

    val subjects: List<String>
)
