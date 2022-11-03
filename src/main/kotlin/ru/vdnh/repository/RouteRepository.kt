package ru.vdnh.repository

import ru.vdnh.model.entity.RouteEntity
import java.math.BigInteger

interface RouteRepository {

    fun getRouteById(id: BigInteger): RouteEntity
}
