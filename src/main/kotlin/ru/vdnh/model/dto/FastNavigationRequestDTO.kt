package ru.vdnh.model.dto

import ru.vdnh.model.enums.LocationPlacement
import ru.vdnh.model.enums.PaymentConditions
import ru.vdnh.model.enums.PopularNavigationType
import ru.vdnh.model.enums.RouteDifficultType
import ru.vdnh.model.enums.VisitorNavigationType

data class FastNavigationRequestDTO(
    val placeNavigation: PlaceNavigationDTO?,
    val dateNavigation: DateNavigationDTO?,

    val visitorType: VisitorNavigationType?,
    val difficulty: RouteDifficultType?,
    val popularity: PopularNavigationType?,
    val placement: LocationPlacement?,
    val paymentConditions: PaymentConditions?,
    val withLoadFactor: Boolean?,

    val subjects: List<String>?
)
