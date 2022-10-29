package ru.vdnh.service

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Service
import ru.vdnh.getLogger
import ru.vdnh.mapper.CoordinatesMapper
import ru.vdnh.model.domain.Location
import ru.vdnh.model.domain.RouteNode
import ru.vdnh.model.dto.CoordinateDTO
import ru.vdnh.model.dto.FastNavigationRequestDTO
import ru.vdnh.model.dto.MapDataDTO
import ru.vdnh.model.dto.DateNavigationDTO
import ru.vdnh.model.dto.PlaceNavigationDTO
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.stream.Collectors


@Service
class NavigationService(
    val locationService: LocationService,
    val coordinatesService: CoordinatesService,
    val graphService: GraphService,
    private val coordinatesMapper: CoordinatesMapper,

    // for test:
    val placesService: PlacesService,
    val eventService: EventService
) : ApplicationRunner {

    // TODO сделать кастомные исключения
    // +/-placeNavigation: PlaceNavigationDTO?,
    // +/-dateNavigation: DateNavigationDTO?,
    //
    // +withLoadFactor: Boolean?,
    // +visitorType: VisitorNavigationType?,
    // +routeSpeedType: RouteSpeedType?,
    // +popularType: PopularNavigationType?,
    // +placement: LocationPlacement?,
    // +paymentConditions: PaymentConditions?,
    fun fastNavigate(dto: FastNavigationRequestDTO): MapDataDTO {
        // берем все места и события
        val locationsBySubjects: List<List<Location>> = dto.subjects
            .map { locationService.getLocationsBySubject(it) }

        // задаем каждой локации приоритет исходя из списка параметров
        // TODO отфильтровать места по расписанию исходя из даты
        val locationsBySubjectsWithPriority: List<List<Pair<Location, Int>>> = locationsBySubjects
            .asSequence()
            .map { it -> it.map { Pair(it, 0) } }
            .map { locationService.addLocationPriorityByLocationType(it) }
            .map { locationService.addLocationPriorityByPopular(it, dto.popularType) }
            .map { locationService.addLocationPriorityByVisitTime(it, dto.routeSpeedType) }
            .map { locationService.addLocationPriorityByVisitorType(it, dto.visitorType) }
            .map { locationService.addLocationPriorityByLocationPlacement(it, dto.placement) }
            .map { locationService.addLocationPriorityByPaymentConditions(it, dto.paymentConditions) }
            .toList()
        if (dto.withLoadFactor == true) {
            locationsBySubjectsWithPriority
                .map { locationService.addLocationPriorityByLoadFactor(it, LocalDate.now().dayOfWeek) }
        }

        // мержим списки по определенным тематикам в один
        // TODO сделать мерж на основе приоритетов
        val locationsByOneSubjectWithPriority: List<Pair<Location, Int>> =
            locationsBySubjectsWithPriority.magicRoundRobin(DEFAULT_ROUND_ROBIN_STRATEGY)

        // сортируем по приоритету
        val sortedLocationsByPriority: List<Location> =
            locationsByOneSubjectWithPriority
                .sortedByDescending { it.second }
                .map { it.first }

        // берем определенное количество локаций исходя из запланированной продолжительности посещения
        // TODO брать из routeSpeedType ???
        val visitDurationMinutes = getVisitDurationMinutes(dto.dateNavigation)
        val locationCount = locationService.getVisitsNumber(sortedLocationsByPriority, visitDurationMinutes)
        val sortedLocationsByPriorityAndVisitDuration: List<Location> =
            sortedLocationsByPriority.take(locationCount)

        // определяем порядок локаций исходя из их местоположения на карте
        val locationRouteList: List<Location> = sortByClosestLocations(
            sortedLocationsByPriorityAndVisitDuration,
            dto.placeNavigation
        )

        val coordinateDtoList: List<CoordinateDTO> = locationRouteList
            .map { coordinatesMapper.domainToDTO(it.coordinates) }
        return MapDataDTO(coordinateDtoList)
    }

    fun getVisitDurationMinutes(
        dates: DateNavigationDTO?,
    ): Int {
        if (dates?.dateFinish == null) {
            return DEFAULT_VISIT_DURATION_MINUTES
        }

        val dateStart = dates.dateStart ?: LocalDateTime.now()
        return dates.dateFinish.toLocalTime().minute - dateStart.toLocalTime().minute
    }

    // val numbers = listOf(listOf(1, 2, 3), listOf(4, 5), listOf(6))
    // numbers.roundRobin(1) // [1, 4, 6, 2, 5, 3]
    // numbers.roundRobin(2) // [1, 2, 4, 5, 6, 3]
    // numbers.roundRobin(3) // [1, 2, 3, 4, 5, 6]
    fun <T> List<List<T>>.magicRoundRobin(count: Int): List<T> {
        return flatMap { it.chunked(count).mapIndexed { i, v -> i to v } }
            .sortedBy { it.first }
            .flatMap { it.second }
    }

    fun sortByClosestLocations(
        locations: List<Location>,
        placeNavigation: PlaceNavigationDTO?
    ): List<Location> {
        // определяем точку входа маршрута (по умолчанию - главный вход (центральный павильон))
        val nodeStart: RouteNode =
            coordinatesService.getRouteNodeByCoordinateId(
                placeNavigation?.startCoordinateId ?: BigInteger.valueOf(
                    DEFAULT_ENTER_NODE_ID
                )
            )

        // определяем точку выхода маршрута
        // TODO подбирать точку выхода с маршрута по дефолту ближайшую к любой точке входа/выхода
        var nodeFinish: RouteNode? = null
        if (placeNavigation?.finishCoordinatesId != null) {
            nodeFinish = coordinatesService.getRouteNodeByCoordinateId(placeNavigation.finishCoordinatesId)
        }

        // создаем граф на основе точек маршрута
        val routeNodes = locations.stream()
            .map { coordinatesService.getRouteNodeByCoordinateId(BigInteger.valueOf(it.coordinates.id)) }
            .collect(Collectors.toList())
        val graph = graphService.createGraphFromRouteNodes(routeNodes)

        // определяем кратчайший путь между точками маршрута
        val sortedLocations: List<Location>
        if (nodeFinish == null) {
            sortedLocations = graphService.getClosestRoute(graph, nodeStart)
                .map { locations.find { location -> location.coordinates.id == it.coordinatesId }!! }
        } else {
            // TODO подобрать алгоритм под нахождение кратчайшего пути с конечной точкой
            throw RuntimeException("TODO(Not yet implemented)")
        }

        return sortedLocations
    }

    // TEST
    override fun run(args: ApplicationArguments?) {
        val test = fastNavigate(
            FastNavigationRequestDTO(
                null,
                null,

                null,
                null,
                null,
                null,
                null,
                null,

                listOf("TEST")
            )
        )

        test.mapData
            .map { it as CoordinateDTO }
            .forEach {
                if (placesService.exist(it.id!!)) {
                    val place = placesService.getByCoordinatesId(it.id)
                    log.info("$place")
                } else {
                    val event = eventService.getByCoordinatesId(it.id)
                    log.info("$event")
                }
            }
    }

    companion object {
        private val log = getLogger<NavigationService>()

        const val DEFAULT_VISIT_DURATION_MINUTES = 120
        const val DEFAULT_ROUND_ROBIN_STRATEGY = 1
        const val DEFAULT_ENTER_NODE_ID = 80L
    }

}
