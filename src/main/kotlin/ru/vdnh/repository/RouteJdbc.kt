package ru.vdnh.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.vdnh.model.entity.RouteEntity
import ru.vdnh.repository.mapper.RouteRowMapper

@Repository
class RouteJdbc(
    private val jdbcTemplate: JdbcTemplate,
    private val routeRowMapper: RouteRowMapper
) : RouteRepository {

    override fun getRouteById(id: Long): RouteEntity {
        return jdbcTemplate.queryForObject(
            "$SQL_SELECT_ENTITY WHERE id = ?", routeRowMapper,
            id
        )!!
    }

    companion object {
        const val SQL_SELECT_ENTITY = "SELECT id, name, description, distance_meters, duration_minutes FROM route"
    }
}
