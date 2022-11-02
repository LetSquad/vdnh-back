package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.LocationMapper
import ru.vdnh.model.domain.Location
import ru.vdnh.model.enums.PopularNavigationType
import ru.vdnh.model.enums.RouteSpeedType
import ru.vdnh.model.enums.VisitorNavigationType
import ru.vdnh.model.enums.LocationPlacement
import ru.vdnh.model.enums.PaymentConditions
import java.time.LocalDateTime


@Service
class LocationService(
    val placeService: PlaceService,
    val eventService: EventService,
    val priorityService: PriorityService,
    val locationMapper: LocationMapper
) {

    fun getLocationsBySubject(subjectCode: String): List<Location> {
        val locationsBySubject = mutableListOf<Location>()
        locationsBySubject.addAll(placeService.getActivePlacesBySubject(subjectCode)
            .map { locationMapper.placeToLocation(it) }
        )
        // TODO продумать нормальную фильтрацию событий
        locationsBySubject.addAll(eventService.getActiveEventsBySubject(subjectCode)
            .filter { it.places != null && it.places.size == 1 }
            .map { locationMapper.eventToLocation(it) }
        )

        return locationsBySubject
    }

    fun addLocationPriorityByLocationType(
        locationsWithPriority: List<Pair<Location, Int>>,
    ): List<Pair<Location, Int>> {
        return locationsWithPriority
            .map { Pair(it.first, priorityService.getPriorityByLocationType(it.first, it.first.locationCodeType)) }
    }

    fun addLocationPriorityByPopular(
        locationsWithPriority: List<Pair<Location, Int>>,
        popularType: PopularNavigationType?,
    ): List<Pair<Location, Int>> {
        return locationsWithPriority
            .map { Pair(it.first, priorityService.getPriorityByPopular(it.first, popularType)) }
    }

    fun addLocationPriorityByVisitTime(
        locationsWithPriority: List<Pair<Location, Int>>,
        routeSpeedType: RouteSpeedType?,
    ): List<Pair<Location, Int>> {
        return locationsWithPriority
            .map { Pair(it.first, priorityService.getPriorityByRouteSpeed(it.first, routeSpeedType)) }
    }

    fun addLocationPriorityByVisitorType(
        locationsWithPriority: List<Pair<Location, Int>>,
        visitorType: VisitorNavigationType?,
    ): List<Pair<Location, Int>> {
        return locationsWithPriority
            .map { Pair(it.first, priorityService.getPriorityByVisitorType(it.first, visitorType)) }
    }

    fun addLocationPriorityByLocationPlacement(
        locationsWithPriority: List<Pair<Location, Int>>,
        locationPlacement: LocationPlacement?,
    ): List<Pair<Location, Int>> {
        return locationsWithPriority
            .map { Pair(it.first, priorityService.getPriorityByLocationPlacement(it.first, locationPlacement)) }
    }

    fun addLocationPriorityByPaymentConditions(
        locationsWithPriority: List<Pair<Location, Int>>,
        paymentConditions: PaymentConditions?,
    ): List<Pair<Location, Int>> {
        return locationsWithPriority
            .map { Pair(it.first, priorityService.getPriorityByPaymentConditions(it.first, paymentConditions)) }
    }

    fun addLocationPriorityByLoadFactor(
        locationsWithPriority: List<Pair<Location, Int>>,
        dateTime: LocalDateTime
    ): List<Pair<Location, Int>> {
        return locationsWithPriority
            .map { Pair(it.first, priorityService.getPriorityByLoadFactor(it.first, dateTime)) }
    }

    // TODO как сделать красивше в котлине ??
    fun getVisitsNumber(
        locations: List<Location>,
        visitDurationMinutes: Int
    ): Int {
        var i = 0
        var count = 0
        var summaryDuration = 0L

        while (summaryDuration < visitDurationMinutes) {
            count++
            summaryDuration += locations[i].visitTime?.toMinutes() ?: 0
            i++
        }

        return count
    }
}
