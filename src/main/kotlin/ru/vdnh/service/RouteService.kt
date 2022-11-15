package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.LocationMapper
import ru.vdnh.mapper.RouteMapper
import ru.vdnh.model.domain.Location
import ru.vdnh.model.dto.MapRouteDataDTO
import ru.vdnh.model.dto.PreparedRouteDataDTO
import ru.vdnh.model.dto.PreparedRouteNavigationDTO
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
    private val routeMapper: RouteMapper
) {

    fun getPreparedRoute(dto: PreparedRouteNavigationDTO): PreparedRouteDataDTO {
        // получаем список готового маршрута
        val routeEntity = routeRepository.getRouteById(dto.id)
        val locations = placeService.getPlacesByRouteId(routeEntity.id)
            .map { locationMapper.placeToLocation(it) }
            .toMutableList()

        // добавляем точку входа на маршрут (если есть)
        if (dto.entrance != null) {
            val locationFinish: Location = locationService.getByPlaceId(dto.entrance)
            locations.add(0, locationFinish)
        }

        // добавляем точку выхода с маршрута (если есть)
        if (dto.exit != null) {
            val locationFinish: Location = locationService.getByPlaceId(dto.exit)
            locations.add(locationFinish)
        }

        // добавляем точки построения пути маршрута от mapbox
        val routeDTO = mapboxService.makeRoute(locations, MovementRouteType.WALKING)

        return routeEntity
            .let { routeMapper.entityToPreparedRouteDomain(it) }
            .let { routeMapper.domainToPreparedDTO(it, routeDTO) }
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
            resultLocationVariants.map { routeNavigateService.makeRouteSort(it, dto.entrance, dto.exit) }

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
