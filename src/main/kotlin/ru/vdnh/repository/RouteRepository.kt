package ru.vdnh.repository

import ru.vdnh.model.entity.RouteEntity

interface RouteRepository {

    fun getRouteById(id: Long): RouteEntity

    fun getAllPreparedRoute(): List<RouteEntity>
}
