package ru.vdnh.mapper

import org.springframework.stereotype.Component
import ru.vdnh.model.domain.Coordinates
import ru.vdnh.model.domain.Event
import ru.vdnh.model.domain.Location
import ru.vdnh.model.domain.Place
import ru.vdnh.model.enums.CategoryType


@Component
class LocationMapper{

    fun placeToLocation(domain: Place) = Location(
        locationId = domain.id,
        coordinates = domain.coordinates,
        locationCodeType = CategoryType.PLACE,
        schedule = domain.schedule,
        visitTime = domain.visitTime,
        placement = domain.placement,
        paymentConditions = domain.paymentConditions,
        priority = domain.priority,
        subjectCode = domain.subjectCode,
        typeCode = domain.typeCode,
        description = mapOf(
            KEY_DESCRIPTION_RU to domain.description,
            KEY_DESCRIPTION_EN to domain.descriptionEn,
            KEY_DESCRIPTION_CN to domain.descriptionCn
        )
    )

    fun eventToLocation(domain: Event) = Location(
        locationId = domain.id,
        coordinates = getCoordinates(domain),
        locationCodeType = CategoryType.EVENT,
        schedule = domain.schedule,
        visitTime = domain.visitTime,
        placement = domain.placement,
        paymentConditions = domain.paymentConditions,
        priority = domain.priority,
        subjectCode = domain.subjectCode,
        typeCode = domain.typeCode,
        description = null
    )

    fun getCoordinates(domain: Event): Coordinates {
        if (domain.coordinates != null) {
            return domain.coordinates
        }

        return domain.places[0].coordinates
    }

    companion object {
        private const val KEY_DESCRIPTION_RU = "descriptionRu"
        private const val KEY_DESCRIPTION_EN = "descriptionEn"
        private const val KEY_DESCRIPTION_CN = "descriptionCn"
    }

}
