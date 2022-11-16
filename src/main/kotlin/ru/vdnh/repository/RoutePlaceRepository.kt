package ru.vdnh.repository

import ru.vdnh.model.entity.RoutePlaceEntity

interface RoutePlaceRepository {

    fun getByRouteId(id: Long): List<RoutePlaceEntity>
}
