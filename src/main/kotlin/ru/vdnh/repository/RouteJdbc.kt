package ru.vdnh.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.vdnh.model.entity.RouteEntity
import ru.vdnh.repository.mapper.RouteRowMapper
import java.math.BigInteger

@Repository
class RouteJdbc(
    private val jdbcTemplate: JdbcTemplate,
    private val routeRowMapper: RouteRowMapper
) : RouteRepository {

    override fun getRouteById(id: BigInteger): RouteEntity {
        return jdbcTemplate.queryForObject(
            "$SQL_SELECT_ENTITY WHERE id = ?", routeRowMapper,
            id
        )!!
    }

    companion object {
        const val SQL_SELECT_ENTITY = "SELECT id, name, description, duration, distance from route"
    }
}
