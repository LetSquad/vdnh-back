package ru.vdnh.service

import org.jgrapht.traverse.ClosestFirstIterator
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Service
import ru.vdnh.getLogger
import ru.vdnh.mapper.LocationMapper
import ru.vdnh.mapper.RouteMapper
import ru.vdnh.model.domain.Location
import ru.vdnh.model.domain.RouteNode
import ru.vdnh.model.dto.CoordinatesDto
import ru.vdnh.model.dto.FastNavigationRequestDTO
import java.math.BigInteger
import java.util.stream.Collectors


@Service
class NavigationService(
    val routeService: RouteService,
    val graphService: GraphService,
    val placeService: PlaceService,
    val eventService: EventService,
    val priorityService: PriorityService,

    val routeMapper: RouteMapper,
    val locationMapper: LocationMapper
) : ApplicationRunner {

    fun getCoordinatesListBySubjects(dto: FastNavigationRequestDTO): List<List<CoordinatesDto>> {
        // TODO единый маршрут с разными тематиками (проставление тематики точки)
        if (dto.count / dto.subjects.size == 0) {
            // TODO сделать кастомные исключения
            throw RuntimeException("Too many subjects, too few count")
        }

        return dto.subjects
            .map {
                getCoordinatesBySubject(
                    routeService.getNodeByPlaceId(dto.startPlace),
                    routeService.getNodeByPlaceId(dto.finishPlace),
                    it,
                    dto.count / dto.subjects.size,
                    dto.loadFactorCheck
                )
            }
    }

    fun getCoordinatesBySubject(
        startPlace: RouteNode?,
        finishPlace: RouteNode?,
        subjectCode: String,
        number: Int,
        loadFactorCheck: Boolean
    ): List<CoordinatesDto> {
        log.info("Get list coordinates by subject [$subjectCode] with loadFactor [$loadFactorCheck]")

        val locationsBySubject = mutableListOf<Location>()
        locationsBySubject.addAll(placeService.getActivePlacesBySubject(subjectCode)
            .map { locationMapper.placeToLocation(it) }
        )
        // TODO сделать фильтрацию событий по привязке к месту
        locationsBySubject.addAll(eventService.getActiveEventsBySubject(subjectCode)
            .map { locationMapper.eventToLocation(it) }
        )

        val locationsWithPriority = locationsBySubject.stream()
            .map { Pair<Location, Int>(it, priorityService.getPriority(it, loadFactorCheck)) }
            .collect(Collectors.toList())

        locationsWithPriority.sortByDescending { it.second }

        val listN = locationsWithPriority.take(number).stream()
            .map { it.first }
            .collect(Collectors.toList())

        return sortByDistance(listN, startPlace, finishPlace)
    }

    fun sortByDistance(
        locations: List<Location>,
        startPlace: RouteNode?,
        finishPlace: RouteNode?
    ): List<CoordinatesDto> {
        val listCoordinates = locations.stream()
            .map { routeService.getNode(BigInteger.valueOf(it.coordinatesId)) }
            .collect(Collectors.toList())

        val graph = graphService.createUndirectedWeightGraph(listCoordinates)

        val result = mutableListOf<CoordinatesDto>()
        // TODO: update alg and add finishPlace
        ClosestFirstIterator(graph, startPlace ?: routeService.DEFAULT_START_NODE).forEach {
            result.add(routeMapper.nodeDomainToCoordinates(it))

            // TODO: test (work only on places)
            log.info("${placeService.getByCoordinatesId(it.coordinatesId)}")
        }

        return result
    }

    // TODO: test
    override fun run(args: ApplicationArguments?) {
        val test = getCoordinatesBySubject(
            routeService.DEFAULT_START_NODE,
            null,
            "TEST",
            13,
            false
        )

        log.info("$test")
    }

    companion object {
        private val log = getLogger<NavigationService>()
    }
}
