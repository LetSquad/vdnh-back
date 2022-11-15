package ru.vdnh.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import ru.vdnh.config.properties.VdnhConfigProperties
import ru.vdnh.model.domain.PreparedRoute
import ru.vdnh.model.domain.RouteNode
import ru.vdnh.model.dto.PreparedRouteDataDTO
import ru.vdnh.model.dto.PreparedRoutePreviewDataDTO
import ru.vdnh.model.dto.RouteDTO
import ru.vdnh.model.entity.CoordinatesEntity
import ru.vdnh.model.entity.RouteEntity

@Component
class RouteMapper(private val mapper: ObjectMapper, private val vdnhConfigProperties: VdnhConfigProperties) {

    fun coordinatesEntityToNodeDomain(coordinates: CoordinatesEntity): RouteNode {
        return RouteNode(
            coordinatesId = coordinates.id,
            latitude = coordinates.latitude,
            longitude = coordinates.longitude,
            connectedCoordinatesId = coordinates.connections.let { mapper.readValue(it) } ?: emptyList()
        )
    }

    fun entityToPreparedRouteDomain(routeEntity: RouteEntity): PreparedRoute = PreparedRoute(
        id = routeEntity.id,
        title = routeEntity.title,
        titleEn = routeEntity.titleEn,
        titleCn = routeEntity.titleCn,
        description = routeEntity.description,
        descriptionEn = routeEntity.descriptionEn,
        descriptionCn = routeEntity.descriptionCn,
        imageUrl = vdnhConfigProperties.baseUrl + routeEntity.imageUrl,
        previewImageUrl = vdnhConfigProperties.baseUrl + routeEntity.previewImageUrl,
        durationSeconds = routeEntity.durationSeconds
    )

    fun domainToPreparedDTO(preparedRoute: PreparedRoute, routeDto: RouteDTO): PreparedRouteDataDTO =
        PreparedRouteDataDTO(
            id = preparedRoute.id,
            title = mapOf(
                vdnhConfigProperties.keyTitleRu to preparedRoute.title,
                vdnhConfigProperties.keyTitleEn to preparedRoute.titleEn,
                vdnhConfigProperties.keyTitleCn to preparedRoute.titleCn
            ),
            description = mapOf(
                KEY_DESCRIPTION_RU to preparedRoute.description,
                KEY_DESCRIPTION_EN to preparedRoute.descriptionEn,
                KEY_DESCRIPTION_CN to preparedRoute.descriptionCn
            ),
            imageUrl = preparedRoute.imageUrl,
            route = routeDto
        )

    fun domainToPreparedPreviewDTO(preparedRoute: PreparedRoute): PreparedRoutePreviewDataDTO =
        PreparedRoutePreviewDataDTO(
            id = preparedRoute.id,
            title = mapOf(
                vdnhConfigProperties.keyTitleRu to preparedRoute.title,
                vdnhConfigProperties.keyTitleEn to preparedRoute.titleEn,
                vdnhConfigProperties.keyTitleCn to preparedRoute.titleCn
            ),
            previewImageUrl = preparedRoute.previewImageUrl,
            duration = preparedRoute.durationSeconds
        )

    companion object {
        private const val KEY_DESCRIPTION_RU = "descriptionRu"
        private const val KEY_DESCRIPTION_EN = "descriptionEn"
        private const val KEY_DESCRIPTION_CN = "descriptionCn"
    }
}
