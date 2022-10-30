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
import ru.vdnh.repository.CoordinatesRepository
import java.util.stream.Collectors


@Service
class NavigationService(
    val graphService: GraphService,
    val placeService: PlaceService,
    val eventService: EventService,
    val priorityService: PriorityService,

    val coordinatesRepository: CoordinatesRepository,

    val routeMapper: RouteMapper,
    val locationMapper: LocationMapper
) : ApplicationRunner {

    // TODO: enterPlace flag in DB
    fun getCoordinatesBySubject(
        enterPlace: RouteNode,
        subjectCode: String,
        number: Int,
        loadingFactorCheck: Boolean
    ): List<CoordinatesDto> {
        log.info("Get list coordinates by subject [$subjectCode] with loadFactor [$loadingFactorCheck]")

        val locationsBySubject = mutableListOf<Location>()
        locationsBySubject.addAll(placeService.getActivePlacesBySubject(subjectCode)
            .map { locationMapper.toLocation(it) }
        )
        // TODO сделать фильтрацию событий по привязке к месту
        locationsBySubject.addAll(eventService.getActiveEventsBySubject(subjectCode)
            .map { locationMapper.toLocation(it) }
        )

        val locationsWithPriority = locationsBySubject.stream()
            .map { Pair<Location, Int>(it, priorityService.getPriority(it, loadingFactorCheck)) }
            .collect(Collectors.toList())

        locationsWithPriority.sortByDescending { it.second }

        val listN = locationsWithPriority.take(number).stream()
            .map { it.first }
            .collect(Collectors.toList())

        return sortByDistance(listN, enterPlace)
    }

    fun sortByDistance(locations: List<Location>, enterPlace: RouteNode): List<CoordinatesDto> {
        val listCoordinates = locations.stream()
            .map {
                val coordinatesId = it.coordinatesId
                coordinatesRepository.get(coordinatesId)
                    .let {
                        routeMapper
                            .coordinatesEntityToNodeDomain(it ?: throw RuntimeException("Coordinates by id $coordinatesId not found"))
                    }
            }
            .collect(Collectors.toList())

        val graph = graphService.createUndirectedWeightGraph(listCoordinates)

        val result = mutableListOf<CoordinatesDto>()
        ClosestFirstIterator(graph, enterPlace).forEach {
            result.add(routeMapper.nodeDomainToCoordinates(it))

            // TODO: test (work only on places)
            log.info("${placeService.getByCoordinatesId(it.coordinatesId)}")
        }

        return result
    }

    // TODO: test
    override fun run(args: ApplicationArguments?) {
        // центральный павильон
        val enter = coordinatesRepository.get(80)
            .let {
                routeMapper.coordinatesEntityToNodeDomain(
                    it ?: throw RuntimeException("Coordinates by id not found")
                )
            }


        val test = getCoordinatesBySubject(
            enter,
            "TEST",
            13,
            false
        )

    }

    companion object {
        private val log = getLogger<NavigationService>()
    }
}
