package ru.vdnh.mapper

import org.springframework.stereotype.Component
import ru.vdnh.model.domain.Event
import ru.vdnh.model.domain.Location
import ru.vdnh.model.domain.Place
import ru.vdnh.model.enums.VisitorNavigationType
import ru.vdnh.model.enums.CategoryType


@Component
class LocationMapper{

    fun placeToLocation(domain: Place) = Location(
        coordinates = domain.coordinates,
        locationCodeType = CategoryType.PLACE,
        schedule = domain.schedule,
        visitorType = VisitorNavigationType.ADULT, // TODO: this
        visitTime = domain.visitTime,
        placement = domain.placement,
        paymentConditions = domain.paymentConditions,
        priority = domain.priority,
    )

    fun eventToLocation(domain: Event) = Location(
        coordinates = domain.coordinates ?: domain.places, // TODO как лучше поступить тут
        locationCodeType = CategoryType.EVENT,
        schedule = domain.schedule,
        visitorType = VisitorNavigationType.ADULT, // TODO: this
        visitTime = domain.visitTime,
        placement = domain.placement,
        paymentConditions = domain.paymentConditions,
        priority = domain.priority,
    )
}
