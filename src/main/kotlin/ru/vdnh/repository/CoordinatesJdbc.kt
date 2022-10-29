package ru.vdnh.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.vdnh.model.domain.LoadFactor
import ru.vdnh.model.entity.CoordinatesEntity

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

    override fun getLoadFactorByCoordinateId(id: Long) =
        jdbcTemplate.queryForObject(
            "SELECT load_factor FROM coordinates WHERE id = $id"
        ) { rs, _ ->
            val jsonb = rs.getString("load_factor")

            // TODO parser

            LoadFactor(1, 2, 3, 1)
        }
}
