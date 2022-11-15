package ru.vdnh.repository

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.vdnh.exception.EntityNotFoundException
import ru.vdnh.model.entity.RouteEntity
import ru.vdnh.repository.mapper.RouteRowMapper

@Repository
class RouteJdbc(
    private val jdbcTemplate: JdbcTemplate,
    private val routeRowMapper: RouteRowMapper
) : RouteRepository {

    override fun getRouteById(id: Long): RouteEntity {
        return try {
            jdbcTemplate.queryForObject(
                "$SQL_SELECT_ENTITY WHERE id = ?", routeRowMapper,
                id
            )!!
        }  catch (ex: EmptyResultDataAccessException) {
            throw EntityNotFoundException("There is no route with id = $id")
        }
    }

    companion object {
        const val SQL_SELECT_ENTITY =
            "SELECT id, name, description, image_url, preview_image_url FROM route"
    }
}
