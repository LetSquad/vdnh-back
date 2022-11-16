package ru.vdnh.repository.mapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import ru.vdnh.model.entity.RoutePlaceEntity
import java.sql.ResultSet

@Component
class RoutePlaceRowMapper : RowMapper<RoutePlaceEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): RoutePlaceEntity = RoutePlaceEntity(
        routeId = rs.getLong("route_id"),
        placeId = rs.getLong("place_id"),
        placeOrder = rs.getInt("place_order"),

        description = rs.getString("description"),
        descriptionEn = rs.getString("description_en"),
        descriptionCn = rs.getString("description_cn")
    )
}
