package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.LocationMapper
import ru.vdnh.model.domain.Location
import ru.vdnh.model.dto.MapRouteDataDTO
import ru.vdnh.model.dto.RouteDTO
import ru.vdnh.model.dto.RouteNavigationDTO
import ru.vdnh.model.enums.MovementRouteType
import ru.vdnh.repository.RouteRepository

@Service
class RouteService(
    private val locationService: LocationService,
    private val placeService: PlaceService,
    private val mapboxService: MapboxService,
    private val routeNavigateService: RouteNavigateService,

    private val routeRepository: RouteRepository,
    private val locationMapper: LocationMapper,
) {

    fun getPreparedRoute(id: Long): MapRouteDataDTO {
        val routeEntity = routeRepository.getRouteById(id)
        val locations = placeService.getPlacesByRouteId(routeEntity.id)
            .map { locationMapper.placeToLocation(it) }

        return MapRouteDataDTO(listOf(mapboxService.makeRoute(locations, MovementRouteType.WALKING)))
    }

    fun getNavigateRoute(dto: RouteNavigationDTO): MapRouteDataDTO {
        val tags: List<String> = routeNavigateService.getTags(dto)
        val locations: List<List<Location>> = locationService.getByTags(tags)
            .map { routeNavigateService.filterLocations(it, dto) }
            .map { routeNavigateService.sortLocationsByPriority(it, dto) }

        // мержим списки по определенным тегам в один общий
        val mergeSortedLocations: List<Location> = routeNavigateService.mergeLocationsByTagsIntoOneList(locations)

        // формируем варианты маршрутов
        val locationVariants: List<List<Location>> =
            routeNavigateService.makeLocationRouteVariants(mergeSortedLocations)

        // определяем итоговые маршруты
        val resultLocationVariants: List<List<Location>> =
            routeNavigateService.takeLocationsByVisitDuration(locationVariants, dto)
        var resultLocations: List<List<Location>> =
            resultLocationVariants.map { routeNavigateService.makeRouteSort(it, dto.startPlaceId, dto.finishPlaceId) }

        // добавляем локации с едой если необходимо
        if (dto.food == true) {
            resultLocations = resultLocations
                .map { routeNavigateService.addFoodLocationsToRoutes(it) }
        }

        // добавляем точки построения пути маршрута от mapbox
        val resultLocationRoutes: List<RouteDTO> = resultLocations
            .map { mapboxService.makeRoute(it, dto.movement ?: MovementRouteType.WALKING) }

        return MapRouteDataDTO(resultLocationRoutes)
    }

}
