package ru.vdnh.service

import org.jgrapht.traverse.ClosestFirstIterator
import org.springframework.stereotype.Service
import ru.vdnh.config.RouteNavigateConfigProperties
import ru.vdnh.mapper.LocationMapper
import ru.vdnh.model.domain.Location
import ru.vdnh.model.domain.RouteNode
import ru.vdnh.model.dto.MapRouteDataDTO
import ru.vdnh.model.dto.RouteDTO
import ru.vdnh.model.dto.RouteNavigationDTO
import ru.vdnh.model.enums.MovementRouteType
import ru.vdnh.repository.RouteRepository
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.stream.Collectors

@Service
class RouteService(
    private val locationService: LocationService,
    private val coordinatesService: CoordinatesService,
    private val graphService: GraphService,
    private val placeService: PlaceService,
    private val mapboxService: MapboxService,

    private val routeRepository: RouteRepository,
    private val locationMapper: LocationMapper,

    private val navigateProperties: RouteNavigateConfigProperties
) {

    fun getPreparedRoute(id: Long): MapRouteDataDTO {
        val routeEntity = routeRepository.getRouteById(id)
        val locations = placeService.getPlacesByRouteId(routeEntity.id)
            .map { locationMapper.placeToLocation(it) }

        return MapRouteDataDTO(listOf(mapboxService.makeRoute(locations, MovementRouteType.WALKING)))
    }

    //    // date
    //    val dateTimeStart: LocalDateTime?,
    //    val dateTimeEnd: LocalDateTime?,
    //
    //    // navigation
    //    +val startPlaceId: Long?,
    //    +val finishPlaceId: Long?,
    //
    //    // criteria
    //    +val peopleNumber: VisitorCountDTO?,
    //    +val popularity: PopularNavigationType?,
    //    +val difficulty: RouteDifficultType?,
    //    +val payment: PaymentConditions?,
    //    +val movement: MovementRouteType?,
    //    +val loadFactor: Boolean?,
    //    val food: Boolean?,
    //    +val tags: List<String>?

    fun updateTagsFromDto(
        tagsFromRequest: List<String>?,
        kids: Boolean?
    ): List<String> {
        if (kids == true) {
            val resultTags: MutableList<String> = tagsFromRequest?.toMutableList() ?: mutableListOf()
            resultTags.add("KIDS")

            return resultTags
        }

        return tagsFromRequest ?: listOf()
    }

    fun getNavigateRoute(dto: RouteNavigationDTO): MapRouteDataDTO {
        val resultTags: List<String> = updateTagsFromDto(dto.tags, dto.peopleNumber?.kid != 0)

        // берем все места и события
        val locations: List<List<Location>> = if (resultTags.isEmpty()) listOf(locationService.getAllActiveLocations()) else resultTags.map { locationService.getLocationsBySubject(it) }

        // задаем каждой локации приоритет исходя из списка параметров
        // TODO отфильтровать места по расписанию исходя из даты
        val locationsWithPriority: List<List<Pair<Location, Int>>> = locations
            .asSequence()
            .map { it -> it.map { Pair(it, navigateProperties.priority.start) } }
            .map { locationService.addLocationPriorityByLocationType(it) }                                  // приоритет типу локации (событие или место)
            .map { locationService.addLocationPriorityByPopular(it, dto.popularity) }                       // приоритет по популярности места
            .map { locationService.addLocationPriorityByRouteDifficulty(it, dto.difficulty) }               // приоритет по сложности маршрута TODO стоит завязать критерий на количество посещаемых мест
//            .map { locationService.addLocationPriorityByVisitorTypeAndNumber(it, dto.peopleNumber) }        // приоритет по количеству посетителей
//            .map { locationService.addLocationPriorityByLocationPlacement(it, LocationPlacement.INDOORS) }  // приоритет по погоде (в помещении, на улице)
            .map { locationService.addLocationPriorityByPaymentRequirements(it, dto.payment) }                // приоритет по типу оплаты TODO стоит сделать фильтрацию по этому критерию
            .toList()
        if (dto.loadFactor == true) {                                                                       // приоритет по загруженности
            val dateTimeNow = LocalDateTime.now()
            locationsWithPriority
                .map { locationService.addLocationPriorityByLoadFactor(it, dateTimeNow) }
        }

        // мержим списки по определенным тематикам в один
        val mergedByTagsLocations: List<Pair<Location, Int>> =
            locationsWithPriority.magicRoundRobin(navigateProperties.locationMergeStrategy)

        // сортируем по приоритету
        val sortedLocationsByPriority: List<Location> = mergedByTagsLocations
                .sortedBy { it.second }
                .map { it.first }

        // формируем варианты маршрутов
        val locationVariants: List<List<Location>> =
            sortedLocationsByPriority
                .withIndex()
                .groupBy { it.index % navigateProperties.routeVariantsCount == 0 }
                .map { entry -> entry.value.map { it.value } }

        // берем определенное количество локаций исходя из запланированной продолжительности посещения
        val localStartTime: LocalDateTime? = dto.dateTimeStart?.atZoneSameInstant(MOSCOW_TIME_ZONE)?.toLocalDateTime()
        val localEndTime: LocalDateTime? = dto.dateTimeEnd?.atZoneSameInstant(MOSCOW_TIME_ZONE)?.toLocalDateTime()
        val visitDurationMinutes = getVisitDurationMinutes(localStartTime, localEndTime)
        val locationCount = locationService.getVisitsNumber(sortedLocationsByPriority, visitDurationMinutes)
        val locationsVariantsForRoute: List<List<Location>> =
            locationVariants.map { it.take(locationCount) }

        // определяем порядок локаций исходя из их местоположения на карте
        val resultLocationRoutes: List<List<Location>> =
            locationsVariantsForRoute.map { sortByClosestLocations(it, dto.startPlaceId, dto.finishPlaceId) }

        // формируем маршрут mapbox
        val resultRoutes: List<RouteDTO> = resultLocationRoutes.map { mapboxService.makeRoute(it, dto.movement ?: MovementRouteType.WALKING) }

        return MapRouteDataDTO(resultRoutes)
    }

    fun getVisitDurationMinutes(
        dateStart: LocalDateTime?,
        dateFinish: LocalDateTime?,
    ): Int {
        if (dateFinish == null || dateStart == null) {
            return navigateProperties.default.visitTimeMinutes
        }

        return dateFinish.toLocalTime().minute - dateStart.toLocalTime().minute
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
        startLocationId: Long?,
        finishLocationId: Long?,
    ): List<Location> {
        // определяем точку входа маршрута (по умолчанию - главный вход (центральный павильон))
        val locationStart: Location =
            locationService.getByPlaceId(startLocationId ?: navigateProperties.default.startPlaceId)
        val nodeStart: RouteNode =
            coordinatesService.getRouteNodeByCoordinateId(locationStart.coordinates.id)

        // определяем точку выхода маршрута
        // TODO подбирать точку выхода с маршрута по дефолту ближайшую к любой точке входа/выхода
//        val locationFinish: Location =
//            locationService.getByPlaceId(placeNavigation?.startPlaceId ?: DEFAULT_FINISH_PLACE_ID)
//        val nodeFinish: RouteNode =
//            coordinatesService.getRouteNodeByCoordinateId(locationFinish.coordinates.id)

        // создаем граф на основе точек маршрута
        val routeNodes = locations.stream()
            .map { coordinatesService.getRouteNodeByCoordinateId(it.coordinates.id) }
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
        private val MOSCOW_TIME_ZONE = ZoneId.of("UTC+3")
    }
}
