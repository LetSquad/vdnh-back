package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.LocationMapper
import ru.vdnh.model.dto.MapRouteDataDTO
import ru.vdnh.repository.RouteRepository

@Service
class RouteService(
    private val routeRepository: RouteRepository,
    private val placeService: PlaceService,
    private val mapboxService: MapboxService,
    private val locationMapper: LocationMapper
) {

    fun getPreparedRoute(id: Long): MapRouteDataDTO {
        val routeEntity = routeRepository.getRouteById(id)
        val locations = placeService.getPlacesByRouteId(routeEntity.id)
            .map { locationMapper.placeToLocation(it) }

        return mapboxService.makeRoute(locations)
    }
}
