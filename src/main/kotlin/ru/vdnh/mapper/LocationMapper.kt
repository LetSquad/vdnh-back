package ru.vdnh.mapper

import org.springframework.stereotype.Component
import ru.vdnh.model.domain.Coordinates
import ru.vdnh.model.domain.Event
import ru.vdnh.model.domain.Location
import ru.vdnh.model.domain.Place
import ru.vdnh.model.dto.GeometryRouteDTO
import ru.vdnh.model.dto.MapPointDTO
import ru.vdnh.model.dto.RouteDTO
import ru.vdnh.model.enums.CategoryType
import ru.vdnh.model.enums.VisitorNavigationType


@Component
class LocationMapper{

    fun placeToLocation(domain: Place) = Location(
        locationId = domain.id,
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
        locationId = domain.id,
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

        return domain.places[0].coordinates
    }

    fun locationsToRouteDTO(locations: List<Location>) = RouteDTO(
        GeometryRouteDTO(
            "LineString",
            locations.map { listOf(it.coordinates.longitude, it.coordinates.latitude) }
        ),

        locations.map { MapPointDTO(it.locationId, it.locationCodeType) }
    )
}
