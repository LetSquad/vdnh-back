package ru.vdnh.repository

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import ru.vdnh.model.entity.RouteEntity
import java.math.BigInteger

interface RouteRepository : CrudRepository<RouteEntity, BigInteger> {
    @Query("select * from coordinates where id = :id")
    fun findRoute(id: BigInteger): RouteEntity
}