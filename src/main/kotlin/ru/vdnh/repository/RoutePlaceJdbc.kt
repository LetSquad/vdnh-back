package ru.vdnh.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.vdnh.model.entity.RoutePlaceEntity
import ru.vdnh.repository.mapper.RoutePlaceRowMapper

@Repository
class RoutePlaceJdbc(
    private val jdbcTemplate: JdbcTemplate,
    private val routePlaceRowMapper: RoutePlaceRowMapper
) : RoutePlaceRepository {

    override fun getByRouteId(id: Long): List<RoutePlaceEntity> {
        return jdbcTemplate.query(
            "$SQL_SELECT_ENTITY WHERE route_id = ? ORDER BY place_order", routePlaceRowMapper,
            id
        )
    }

    companion object {
        const val SQL_SELECT_ENTITY =
            "SELECT route_id, place_id, place_order, description, description_en, description_cn FROM route_place"
    }
}
