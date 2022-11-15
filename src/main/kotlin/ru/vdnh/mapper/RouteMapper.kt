package ru.vdnh.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import ru.vdnh.model.domain.PreparedRoute
import ru.vdnh.model.domain.RouteNode
import ru.vdnh.model.dto.PreparedRouteDataDTO
import ru.vdnh.model.dto.RouteDTO
import ru.vdnh.model.entity.CoordinatesEntity
import ru.vdnh.model.entity.RouteEntity

@Component
class RouteMapper(private val mapper: ObjectMapper) {

    fun coordinatesEntityToNodeDomain(coordinates: CoordinatesEntity): RouteNode {
        return RouteNode(
            coordinatesId = coordinates.id,
            latitude = coordinates.latitude!!, // TODO ?
            longitude = coordinates.longitude!!, // TODO ?
            connectedCoordinatesId = coordinates.connections?.let { mapper.readValue(it) } ?: emptyList()
        )
    }

    fun entityToPreparedRouteDomain(routeEntity: RouteEntity): PreparedRoute = PreparedRoute(
        name = routeEntity.name,
        description = routeEntity.description,
        imageUrl = VDNH_BASE_URL + routeEntity.imageUrl,
        previewImageUrl = VDNH_BASE_URL + routeEntity.previewImageUrl
    )

    fun domainToPreparedDTO(preparedRoute: PreparedRoute, routeDto: RouteDTO): PreparedRouteDataDTO =
        PreparedRouteDataDTO(
            name = preparedRoute.name,
            description = preparedRoute.description,
            imageUrl = preparedRoute.imageUrl,
            previewImageUrl = preparedRoute.previewImageUrl,
            mapData = routeDto
        )

    companion object {
        private const val VDNH_BASE_URL = "https://vdnh.ru"
    }

}
