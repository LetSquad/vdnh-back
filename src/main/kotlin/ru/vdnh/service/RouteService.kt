package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.PlaceMapper
import ru.vdnh.model.dto.PlaceDTOList
import ru.vdnh.repository.RouteRepository

@Service
class RouteService(
    private val placeMapper: PlaceMapper,
    private val routeRepository: RouteRepository,
    private val placeService: PlaceService
) {

    fun getRoute(id: Long): PlaceDTOList {
        val routeEntity = routeRepository.getRouteById(id)
        val placeDTOList = placeService.getPlacesByRouteId(routeEntity.id)
            .map { placeMapper.domainToDto(it) }

        return PlaceDTOList(placeDTOList)
    }
}
