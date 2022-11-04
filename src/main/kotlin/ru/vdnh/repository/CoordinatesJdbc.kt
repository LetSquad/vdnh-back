package ru.vdnh.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.vdnh.model.entity.CoordinatesEntity
import ru.vdnh.repository.mapper.CoordinatesRowMapper

@Repository
class CoordinatesJdbc(
    private val jdbcTemplate: JdbcTemplate,
    private val coordinatesRowMapper: CoordinatesRowMapper
) : CoordinatesRepository {

    override fun getAllCoordinates(): List<CoordinatesEntity> {
        return jdbcTemplate.query(
            SQL_SELECT_ENTITY,
            coordinatesRowMapper
        )
    }

    override fun getCoordinatesById(id: Long): CoordinatesEntity {
        return jdbcTemplate.queryForObject(
            "$SQL_SELECT_ENTITY WHERE id = ?",
            coordinatesRowMapper, id
        )!!
    }

    companion object {
        const val SQL_SELECT_ENTITY = "SELECT id coordinates_id, latitude, longitude, connections, load_factor FROM coordinates"
    }

}
