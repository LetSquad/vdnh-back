package ru.vdnh.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.vdnh.model.entity.CoordinatesEntity
import java.math.BigInteger

@Repository
class CoordinatesJdbc(private val jdbcTemplate: JdbcTemplate) : CoordinatesRepository {

    override fun getAllCoordinates(): List<CoordinatesEntity> {
        return jdbcTemplate.query(
            "SELECT id, latitude, longitude, connections, load_factor FROM coordinates"
        ) { rs, _ ->
            CoordinatesEntity(
                id = rs.getLong("id"),
                latitude = rs.getBigDecimal("latitude"),
                longitude = rs.getBigDecimal("longitude"),
                connections = rs.getString("connections"),
                loadFactor = rs.getString("load_factor")
            )
        }
    }

    override fun getCoordinatesById(id: BigInteger): CoordinatesEntity {
        return jdbcTemplate.queryForObject(
            "SELECT id, latitude, longitude, connections, load_factor FROM coordinates where id = ?"
        ) { rs, _ ->
            CoordinatesEntity(
                id = rs.getLong("id"),
                latitude = rs.getBigDecimal("latitude"),
                longitude = rs.getBigDecimal("longitude"),
                connections = rs.getString("connections"),
                loadFactor = rs.getString("load_factor")
            )
        }!!
    }
}
