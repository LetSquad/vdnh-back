package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.LocationMapper
import ru.vdnh.model.domain.Location


@Service
class LocationService(
    private val placeService: PlaceService,
    private val eventService: EventService,
    private val locationMapper: LocationMapper
) {

    fun getByPlaceId(placeId: Long): Location {
        return placeService.getPlaceById(placeId)
            .let { locationMapper.placeToLocation(it) }
    }

    fun getByPlaceCoordinateId(placeCoordinateId: Long): Location {
        return placeService.getPlaceByCoordinateId(placeCoordinateId)
            .let { locationMapper.placeToLocation(it) }
    }

    fun getByTags(tags: List<String>): List<List<Location>> {
        return if (tags.isEmpty()) listOf(getLocationsBySubject(null)) else tags.map { getLocationsBySubject(it) }
    }

    fun getLocationsBySubject(subjectCode: String?): List<Location> {
        val places =
            if (subjectCode != null) placeService.getActivePlacesBySubject(subjectCode) else placeService.getAllActivePlaces()
        val locationsBySubject = mutableListOf<Location>()
        locationsBySubject.addAll(
            places
                .filter { !BLACK_SET_TYPE_CODES.contains(it.typeCode) }
                .map { locationMapper.placeToLocation(it) }
        )

        val events =
            if (subjectCode != null) eventService.getActiveEventsBySubject(subjectCode) else eventService.getAllActiveEvents()
        locationsBySubject.addAll(
            events
                .filter { !BLACK_SET_TYPE_CODES.contains(it.typeCode) }
                .filter { it.places.size == 1 }
                .map { locationMapper.eventToLocation(it) }
        )

        return locationsBySubject
    }

    fun getVisitsNumber(
        locations: List<Location>,
        visitDurationMinutes: Int
    ): Int {
        var summaryDuration = 0L

        locations.forEachIndexed { i, location ->
            summaryDuration += location.visitTime?.toMinutes() ?: 0
            if (summaryDuration > visitDurationMinutes) {
                return i + 1
            }

        }
        return locations.size
    }

    fun getAllFoodLocations(): List<Location> {
        return placeService.getPlacesByType(FOOD_TYPE_CODE)
            .map { locationMapper.placeToLocation(it) }
    }

    companion object {
        private val BLACK_SET_TYPE_CODES = setOf(
            "ENTRANCE",
            "ENTRY",
            "TAXI",
            "BUS_STOP",
            "PARKING",

            "WC",
            "FIRST_AID",

            "FOOD",
            "VENDING",
            "ATM",
            "SOUVENIRS",
            "TICKETS",
            "PICNIC",
            "READING_ROOM",
            "HIRE",

            "OTHER",
            "INFOCENTER",
            "ONLINE"
        )
        private val FOOD_TYPE_CODE = "FOOD"
    }
}
