package ru.vdnh.mapper

import org.springframework.stereotype.Component
import ru.vdnh.model.domain.Coordinates
import ru.vdnh.model.domain.Event
import ru.vdnh.model.domain.Location
import ru.vdnh.model.domain.Place
import ru.vdnh.model.enums.CategoryType
import ru.vdnh.model.enums.VisitorNavigationType


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
        coordinates = getCoordinates(domain),
        locationCodeType = CategoryType.EVENT,
        schedule = domain.schedule,
        visitorType = VisitorNavigationType.ADULT, // TODO: this
        visitTime = domain.visitTime,
        placement = domain.placement,
        paymentConditions = domain.paymentConditions,
        priority = domain.priority,
    )

    fun getCoordinates(domain: Event): Coordinates {
        if (domain.coordinates != null) {
            return domain.coordinates
        }
        if (domain.places != null) {
            return domain.places[0].coordinates
        }

        throw RuntimeException("Event ${domain.id} has no coordinates")
    }
}
