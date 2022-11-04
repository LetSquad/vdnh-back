package ru.vdnh.service

import org.jgrapht.traverse.ClosestFirstIterator
import org.springframework.stereotype.Service
import ru.vdnh.model.domain.Location
import ru.vdnh.model.domain.RouteNode
import ru.vdnh.model.dto.DateNavigationDTO
import ru.vdnh.model.dto.FastNavigationRequestDTO
import ru.vdnh.model.dto.MapRouteDataDTO
import ru.vdnh.model.dto.PlaceNavigationDTO
import java.math.BigInteger
import java.time.LocalDateTime
import java.util.stream.Collectors


@Service
class NavigationService(
    val locationService: LocationService,
    val coordinatesService: CoordinatesService,
    val graphService: GraphService,
    val mapboxService: MapboxService,
) {

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
    fun fastNavigate(dto: FastNavigationRequestDTO): MapRouteDataDTO {
        // берем все места и события
        val locationsBySubjects: List<List<Location>> =
            if (dto.subjects != null)
                dto.subjects
                    .map { locationService.getLocationsBySubject(it) }
            else
                listOf(locationService.getAllLocations())

        // задаем каждой локации приоритет исходя из списка параметров
        // TODO отфильтровать места по расписанию исходя из даты
        val locationsBySubjectsWithPriority: List<List<Pair<Location, Int>>> = locationsBySubjects
            .asSequence()
            .map { it -> it.map { Pair(it, 0) } }
            .map { locationService.addLocationPriorityByLocationType(it) }
            .map { locationService.addLocationPriorityByPopular(it, dto.popularity) }
            .map { locationService.addLocationPriorityByRouteDifficulty(it, dto.difficulty) }
            .map { locationService.addLocationPriorityByVisitorType(it, dto.visitorType) }
            .map { locationService.addLocationPriorityByLocationPlacement(it, dto.placement) }
            .map { locationService.addLocationPriorityByPaymentConditions(it, dto.paymentConditions) }
            .toList()
        if (dto.withLoadFactor == true) {
            val dateTimeNow = LocalDateTime.now()
            locationsBySubjectsWithPriority
                .map { locationService.addLocationPriorityByLoadFactor(it, dateTimeNow) }
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
        return mapboxService.makeRoute(locationRouteList)
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
        val locationStart: Location =
            locationService.getByPlaceId(placeNavigation?.startPlaceId ?: DEFAULT_START_PLACE_ID)
        val nodeStart: RouteNode =
            coordinatesService.getRouteNodeByCoordinateId(locationStart.coordinates.id.toBigInteger())

        // определяем точку выхода маршрута
        // TODO подбирать точку выхода с маршрута по дефолту ближайшую к любой точке входа/выхода
//        val locationFinish: Location =
//            locationService.getByPlaceId(placeNavigation?.startPlaceId ?: DEFAULT_FINISH_PLACE_ID)
//        val nodeFinish: RouteNode =
//            coordinatesService.getRouteNodeByCoordinateId(locationFinish.coordinates.id.toBigInteger())

        // создаем граф на основе точек маршрута
        val routeNodes = locations.stream()
            .map { coordinatesService.getRouteNodeByCoordinateId(BigInteger.valueOf(it.coordinates.id)) }
            .collect(Collectors.toList())

        routeNodes.add(nodeStart)
        val locationsWithStart: MutableList<Location> = locations.toMutableList()
        locationsWithStart.add(locationStart)

        val graph = graphService.createGraphFromRouteNodes(routeNodes)

        // определяем кратчайший путь между точками маршрута
        val sortedLocations: MutableList<Location> = mutableListOf()
        ClosestFirstIterator(graph, nodeStart)
            .forEach { sortedLocations.add(locationsWithStart.find { location -> location.coordinates.id == it.coordinatesId }!!) }

        return sortedLocations
    }

    companion object {
        const val DEFAULT_VISIT_DURATION_MINUTES = 120
        const val DEFAULT_ROUND_ROBIN_STRATEGY = 1
        const val DEFAULT_START_PLACE_ID = 334L
        const val DEFAULT_FINISH_PLACE_ID = 334L
    }

}
