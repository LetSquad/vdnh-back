package ru.vdnh.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.vdnh.model.entity.CoordinatesEntity
import ru.vdnh.repository.mapper.CoordinatesRowMapper
import java.math.BigInteger

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

    override fun getCoordinatesById(id: BigInteger): CoordinatesEntity {
        return jdbcTemplate.queryForObject(
            "$SQL_SELECT_ENTITY WHERE id = ?",
            coordinatesRowMapper, id
        )!!
    }

    override fun get(id: Long): CoordinatesEntity? {
        return jdbcTemplate.queryForObject(
            "$SQL_SELECT_ENTITY WHERE id = ?",
            { rs, _ ->
                CoordinatesEntity(
                    id = rs.getLong("id"),
                    latitude = rs.getBigDecimal("latitude"),
                    longitude = rs.getBigDecimal("longitude"),
                    connections = rs.getString("connections"),
                    loadFactor = rs.getString("load_factor")
                )
            }, id)
    }

    companion object {
        const val SQL_SELECT_ENTITY = "SELECT id coordinates_id, latitude, longitude, connections, load_factor FROM coordinates"
    }

}
