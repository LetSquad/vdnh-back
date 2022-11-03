package ru.vdnh.repository.mapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import ru.vdnh.model.entity.RouteEntity
import java.sql.ResultSet

@Component
class RouteRowMapper : RowMapper<RouteEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): RouteEntity = RouteEntity(
        id = rs.getLong("id"),
        name = rs.getString("name"),
        description = rs.getString("description"),
        distance = rs.getInt("distance"),
        duration = rs.getInt("duration")
    )
}
