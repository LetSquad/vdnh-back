package ru.vdnh.service

import org.jgrapht.traverse.ClosestFirstIterator
import org.springframework.stereotype.Service
import ru.vdnh.config.RouteNavigateConfigProperties
import ru.vdnh.model.domain.Location
import ru.vdnh.model.domain.Schedule
import ru.vdnh.model.domain.WorkingHours
import ru.vdnh.model.dto.RouteNavigationDTO
import ru.vdnh.model.enums.PaymentConditions
import ru.vdnh.model.enums.PaymentRequirements
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId


@Service
class RouteNavigateService(
    private val priorityService: PriorityService,
    private val locationService: LocationService,
    private val graphService: GraphService,

    private val navigateProperties: RouteNavigateConfigProperties
) {

    fun getTags(dto: RouteNavigationDTO): List<String> {
        if (dto.peopleNumber?.kid != 0 && !dto.tags.isNullOrEmpty()) {
            val resultTags: MutableList<String> = dto.tags.toMutableList()
            resultTags.add(KIDS_TAG)

            return resultTags
        }

        return dto.tags ?: listOf()
    }

    fun filterLocations(
        locations: List<Location>,
        dto: RouteNavigationDTO
    ): List<Location> {
        var locationsTemp = locations.toList()

        if (dto.payment == PaymentRequirements.FREE) {
            locationsTemp = locationsTemp.filter { it.paymentConditions == PaymentConditions.FREE }
        }

        locationsTemp = locationsTemp.filter { filterByLocationSchedule(it.schedule, dto.dateTimeStart, dto.dateTimeEnd) }

        return locationsTemp

    }

    fun filterByLocationSchedule(
        schedule: Schedule?,
        dateTimeStart: OffsetDateTime?,
        dateTimeEnd: OffsetDateTime?
    ): Boolean {
        if (schedule != null) {
            val localDateTimeStart: LocalDateTime = dateTimeStart?.atZoneSameInstant(MOSCOW_TIME_ZONE)?.toLocalDateTime() ?: LocalDateTime.now()
            val timeStart = localDateTimeStart.toLocalTime()
            val timeFinish: LocalTime? = dateTimeEnd?.atZoneSameInstant(MOSCOW_TIME_ZONE)?.toLocalTime()
            return when (localDateTimeStart.dayOfWeek) {
                DayOfWeek.MONDAY -> filterByTime(schedule.monday, timeStart, timeFinish)
                DayOfWeek.TUESDAY -> filterByTime(schedule.tuesday, timeStart, timeFinish)
                DayOfWeek.WEDNESDAY -> filterByTime(schedule.wednesday, timeStart, timeFinish)
                DayOfWeek.THURSDAY -> filterByTime(schedule.thursday, timeStart, timeFinish)
                DayOfWeek.FRIDAY -> filterByTime(schedule.friday, timeStart, timeFinish)
                DayOfWeek.SUNDAY -> filterByTime(schedule.sunday, timeStart, timeFinish)
                DayOfWeek.SATURDAY -> filterByTime(schedule.saturday, timeStart, timeFinish)
                else -> true
            }
        }

        return true
    }

    fun filterByTime(
        workingHours: WorkingHours?,
        timeStart: LocalTime,
        timeFinish: LocalTime?
    ): Boolean {
        if (workingHours != null) {
            if (workingHours.to != null && workingHours.to.isBefore(timeStart)) {
                return false
            }
            if (workingHours.from != null && timeFinish != null && workingHours.from.isAfter(timeFinish)) {
                return false
            }
        }

        return true
    }

    fun addPriorityToLocation(
        location: Location,
        dto: RouteNavigationDTO
    ): Pair<Location, Int> {
        var priority = location.priority ?: navigateProperties.priority.start

        priority -= priorityService.getPriorityByLocationType(location, location.locationCodeType)
        priority -= priorityService.getPriorityByPopular(location, dto.popularity)
        priority -= priorityService.getPriorityByRouteDifficulty(location, dto.difficulty)
        priority -= priorityService.getPriorityByPlacementType(location, dto.locationPlacement)
        priority -= priorityService.getPriorityByVisitorTypeAndNumber(location, dto.peopleNumber)
        priority -= priorityService.getPriorityByPaymentRequirements(location, dto.payment)

        if (dto.loadFactor == true) {
            priority -= priorityService.getPriorityByLoadFactor(location, LocalDateTime.now())
        }

        return Pair(location, priority)
    }

    fun sortLocationsByPriority(
        locations: List<Location>,
        dto: RouteNavigationDTO
    ): List<Location> {
        return locations
            .map { addPriorityToLocation(it, dto) }
            .sortedBy { it.second }
            .map { it.first }
    }

    fun mergeLocationsByTagsIntoOneList(
        locationsByTags: List<List<Location>>
    ): List<Location> {
        return locationsByTags.magicRoundRobin(navigateProperties.locationMergeStrategy)
    }

    fun makeLocationRouteVariants(
        locations: List<Location>
    ): List<List<Location>> {
        val result: MutableList<List<Location>> = mutableListOf()
        var count = 0
        while (count < navigateProperties.routeVariantsCount) {
            result.add(locations.filterIndexed { i, _ -> i % navigateProperties.routeVariantsCount == count })
            count++
        }
        return result
    }

    fun takeLocationsByVisitDuration(
        locations: List<List<Location>>,
        dto: RouteNavigationDTO
    ): List<List<Location>> {
        val result = mutableListOf<List<Location>>()
        val localStartTime: LocalDateTime? = dto.dateTimeStart?.atZoneSameInstant(MOSCOW_TIME_ZONE)?.toLocalDateTime()
        val localEndTime: LocalDateTime? = dto.dateTimeEnd?.atZoneSameInstant(MOSCOW_TIME_ZONE)?.toLocalDateTime()

        val routeDurationMinutes = getRouteDurationMinutes(localStartTime, localEndTime)

        locations.forEach {
            if (it.size >= navigateProperties.default.minLocationCountInRoute) {
                var locationCount = locationService.getVisitsNumber(it, routeDurationMinutes)
                if (locationCount > navigateProperties.default.maxLocationCountInRoute) {
                    locationCount = navigateProperties.default.maxLocationCountInRoute
                }

                result.add(it.take(locationCount))
            }
        }

        return result
    }

    fun getRouteDurationMinutes(
        dateStart: LocalDateTime?,
        dateFinish: LocalDateTime?,
    ): Int {
        if (dateFinish == null || dateStart == null) {
            return navigateProperties.default.routeTimeMinutes
        }

        return (dateFinish.toLocalTime().toSecondOfDay() - dateStart.toLocalTime().toSecondOfDay()) / MINUTES_IN_HOUR
    }

    fun addFoodLocationsToRoutes(locations: List<Location>): List<Location> {
        var minutes = 0
        val indexesBeforeFood = mutableListOf<Int>()
        val foodLocationToInsert = mutableListOf<Location>()
        locations.forEachIndexed { i, location ->
            if (minutes < navigateProperties.toFoodLocationDurationMinutes) {
                minutes += location.visitTime?.toMinutes()?.toInt() ?: navigateProperties.default.visitTimeMinutes
            } else {
                indexesBeforeFood.add(i + 1)
                val closestFoodLocation: Location = findClosestFoodLocation(location)
                foodLocationToInsert.add(closestFoodLocation)
                minutes = 0
            }
        }

        val resultRouteWithFood = locations.toMutableList()

        if (indexesBeforeFood.isEmpty()) {
            val foodLocation: Location = findClosestFoodLocation(locations.last())
            resultRouteWithFood.add(foodLocation)
        } else {
            foodLocationToInsert.forEachIndexed { i, foodLocation ->
                val indexToInsertFood = indexesBeforeFood[i]
                resultRouteWithFood.add(indexToInsertFood, foodLocation)
            }
        }

        return resultRouteWithFood
    }

    fun findClosestFoodLocation(location: Location): Location {
        val routeNodeToInsertFood = graphService.makeRouteNodeFromLocation(location)
        val routeNodesFood = locationService.getAllFoodLocations()
            .map { graphService.makeRouteNodeFromLocation(it) }
            .toMutableList()
        routeNodesFood.add(routeNodeToInsertFood)

        val foodGraph = graphService.createGraphFromRouteNodes(routeNodesFood)
        val graphIterator = ClosestFirstIterator(foodGraph, routeNodeToInsertFood).iterator()
        graphIterator.next() // пропускаем первый элемент - это всегда точка старта
        return graphIterator.next().let { locationService.getByPlaceCoordinateId(it.coordinatesId) }
    }

    // TODO подбирать точку выхода с маршрута по дефолту ближайшую к любой точке входа/выхода
    fun makeRouteSort(
        locations: List<Location>,
        startLocationId: Long?,
        finishLocationId: Long?,
    ): List<Location> {
        // определяем точку входа маршрута (по умолчанию - главный вход (центральный павильон))
        val locationStart: Location =
            locationService.getByPlaceId(startLocationId ?: navigateProperties.default.startPlaceId)
        val locationsWithStart: MutableList<Location> = locations.toMutableList()
        locationsWithStart.add(locationStart)

        // создаем граф на основе точек маршрута
        val nodeStart = graphService.makeRouteNodeFromLocation(locationStart)
        val nodes = graphService.makeRouteNodesFormLocations(locations)
        nodes.add(nodeStart)

        val graph = graphService.createGraphFromRouteNodes(nodes)

        // определяем кратчайший путь между точками маршрута
        val sortedLocations: MutableList<Location> = mutableListOf()
        ClosestFirstIterator(graph, nodeStart)
            .forEach { sortedLocations.add(locationsWithStart.find { location -> location.coordinates.id == it.coordinatesId }!!) }

        return sortedLocations
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

    companion object {
        private val KIDS_TAG = "KIDS"

        private val MOSCOW_TIME_ZONE = ZoneId.of("UTC+3")
        private val MINUTES_IN_HOUR = 60
    }
}
