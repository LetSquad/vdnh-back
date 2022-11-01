package ru.vdnh.repository.mapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import ru.vdnh.model.entity.CoordinatesEntity
import java.sql.ResultSet

@Component
class CoordinatesRowMapper : RowMapper<CoordinatesEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): CoordinatesEntity = CoordinatesEntity(
        id = rs.getLong("coordinates_id"),
        latitude = rs.getBigDecimal("latitude"),
        longitude = rs.getBigDecimal("longitude"),
        connections = rs.getString("connections"),
        loadFactor = rs.getString("load_factor")
    )
}