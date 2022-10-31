package ru.vdnh.mapper

import org.springframework.stereotype.Component
import ru.vdnh.model.domain.Event
import ru.vdnh.model.domain.Location
import ru.vdnh.model.domain.LocationType
import ru.vdnh.model.domain.Place


@Component
class LocationMapper {

    fun placeToLocation(domain: Place) = Location(
        id = domain.id,
        priority = domain.priority,
        type = LocationType.PLACE,
        coordinatesId = domain.coordinates.id
    )

    fun eventToLocation(domain: Event) = Location(
        id = domain.id,
        priority = domain.priority,
        type = LocationType.EVENT,
        coordinatesId = domain.coordinates?.id ?: 0 // TODO
    )
}
