package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.LocationMapper
import ru.vdnh.model.domain.Location
import ru.vdnh.model.enums.LocationPlacement
import ru.vdnh.model.enums.PaymentRequirements
import ru.vdnh.model.enums.PopularNavigationType
import ru.vdnh.model.enums.RouteDifficultType
import java.time.LocalDateTime


@Service
class LocationService(
    private val placeService: PlaceService,
    private val eventService: EventService,
    private val priorityService: PriorityService,
    private val locationMapper: LocationMapper
) {

    fun getByPlaceId(placeId: Long): Location {
        return placeService.getPlaceById(placeId)
            .let { locationMapper.placeToLocation(it) }
    }

    fun getAllActiveLocations(): List<Location> {
        val locationsBySubject = mutableListOf<Location>()
        locationsBySubject.addAll(placeService.getAllActivePlaces()
            .map { locationMapper.placeToLocation(it) }
        )
        // TODO продумать нормальную фильтрацию событий
        locationsBySubject.addAll(eventService.getAllActiveEvents()
            .filter { it.places.size == 1 }
            .map { locationMapper.eventToLocation(it) }
        )

        return locationsBySubject
    }

    fun getLocationsBySubject(subjectCode: String): List<Location> {
        val locationsBySubject = mutableListOf<Location>()
        locationsBySubject.addAll(placeService.getActivePlacesBySubject(subjectCode)
            .map { locationMapper.placeToLocation(it) }
        )
        // TODO продумать нормальную фильтрацию событий
        locationsBySubject.addAll(eventService.getActiveEventsBySubject(subjectCode)
            .filter { it.places.size == 1 }
            .map { locationMapper.eventToLocation(it) }
        )

        return locationsBySubject
    }

    fun addLocationPriorityByLocationType(
        locationsWithPriority: List<Pair<Location, Int>>,
    ): List<Pair<Location, Int>> {
        return locationsWithPriority
            .map { Pair(it.first, it.second - priorityService.getPriorityByLocationType(it.first, it.first.locationCodeType)) }
    }

    fun addLocationPriorityByPopular(
        locationsWithPriority: List<Pair<Location, Int>>,
        popularType: PopularNavigationType?,
    ): List<Pair<Location, Int>> {
        return locationsWithPriority
            .map { Pair(it.first, it.second - priorityService.getPriorityByPopular(it.first, popularType)) }
    }

    fun addLocationPriorityByRouteDifficulty(
        locationsWithPriority: List<Pair<Location, Int>>,
        routeDifficultType: RouteDifficultType?,
    ): List<Pair<Location, Int>> {
        return locationsWithPriority
            .map { Pair(it.first, it.second - priorityService.getPriorityByRouteSpeed(it.first, routeDifficultType)) }
    }

    fun addLocationPriorityByLocationPlacement(
        locationsWithPriority: List<Pair<Location, Int>>,
        locationPlacement: LocationPlacement?,
    ): List<Pair<Location, Int>> {
        return locationsWithPriority
            .map { Pair(it.first, it.second -priorityService.getPriorityByLocationPlacement(it.first, locationPlacement)) }
    }

    fun addLocationPriorityByPaymentRequirements(
        locationsWithPriority: List<Pair<Location, Int>>,
        paymentRequirements: PaymentRequirements?,
    ): List<Pair<Location, Int>> {
        return locationsWithPriority
            .map { Pair(it.first, it.second -priorityService.getPriorityByPaymentRequirements(it.first, paymentRequirements)) }
    }

    fun addLocationPriorityByLoadFactor(
        locationsWithPriority: List<Pair<Location, Int>>,
        dateTime: LocalDateTime
    ): List<Pair<Location, Int>> {
        return locationsWithPriority
            .map { Pair(it.first, it.second -priorityService.getPriorityByLoadFactor(it.first, dateTime)) }
    }

    fun getVisitsNumber(
        locations: List<Location>,
        visitDurationMinutes: Int
    ): Int {
        var summaryDuration = 0L

        locations
            .forEachIndexed { i, location ->
                summaryDuration += location.visitTime?.toMinutes() ?: 0
                if (summaryDuration > visitDurationMinutes) {
                    return i + 1
                }

            }
        return locations.size
    }
}
