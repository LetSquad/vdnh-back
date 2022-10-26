package ru.vdnh.mapper

import org.springframework.stereotype.Component
import ru.vdnh.model.domain.Route
import ru.vdnh.model.dto.RouteDTO
import ru.vdnh.model.entity.RouteEntity

@Component
class RouteMapper {
    fun entityToDomain(entity: RouteEntity) = Route(
        latitude = entity.latitude,
        longitude = entity.longitude
    )

    fun domainToDTO(domain: Route) = RouteDTO(
        latitude = domain.latitude,
        longitude = domain.longitude
    )
}