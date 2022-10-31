package ru.vdnh.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import ru.vdnh.model.domain.Route
import ru.vdnh.model.domain.RouteNode
import ru.vdnh.model.dto.CoordinatesDto
import ru.vdnh.model.dto.RouteDTO
import ru.vdnh.model.entity.CoordinatesEntity

@Component
class RouteMapper(private val mapper: ObjectMapper) {

    fun entityToDomain(entity: CoordinatesEntity) = Route(
        latitude = entity.latitude,
        longitude = entity.longitude
    )

    fun domainToDTO(domain: Route) = RouteDTO(
        latitude = domain.latitude,
        longitude = domain.longitude
    )

    fun coordinatesEntityToNodeDomain(coordinates: CoordinatesEntity): RouteNode {
        return RouteNode(
            coordinatesId = coordinates.id,
            latitude = coordinates.latitude,
            longitude = coordinates.longitude,
            connectedCoordinatesId = coordinates.connections?.let { mapper.readValue(it) } ?: emptyList()
        )
    }

    fun nodeDomainToCoordinates(routeNode: RouteNode): CoordinatesDto {
        return CoordinatesDto(
            id = routeNode.coordinatesId,
            latitude = routeNode.latitude,
            longitude = routeNode.longitude,
        )
    }

}
