package ru.vdnh.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import ru.vdnh.model.domain.RouteNode
import ru.vdnh.model.entity.CoordinatesEntity

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

}
