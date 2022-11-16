package ru.vdnh.mapper

import org.springframework.stereotype.Component
import ru.vdnh.model.domain.RoutePlace
import ru.vdnh.model.entity.RoutePlaceEntity

@Component
class PlaceRouteMapper {

    fun entityToDomain(routePlaceEntity: RoutePlaceEntity): RoutePlace = RoutePlace(
        routeId = routePlaceEntity.routeId,
        placeId = routePlaceEntity.placeId,
        placeOrder = routePlaceEntity.placeOrder,
        description = routePlaceEntity.description,
        descriptionEn = routePlaceEntity.descriptionEn,
        descriptionCn = routePlaceEntity.descriptionCn
    )
}
