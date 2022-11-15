package ru.vdnh.repository.mapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import ru.vdnh.model.entity.RouteEntity
import java.sql.ResultSet

@Component
class RouteRowMapper : RowMapper<RouteEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): RouteEntity = RouteEntity(
        id = rs.getLong("id"),

        title = rs.getString("name"),
        titleEn = rs.getString("name_en"),
        titleCn = rs.getString("name_cn"),

        description = rs.getString("description"),
        descriptionEn = rs.getString("description_en"),
        descriptionCn = rs.getString("description_cn"),

        imageUrl = rs.getString("image_url"),
        previewImageUrl = rs.getString("preview_image_url"),

        durationSeconds = rs.getInt("duration_seconds")
    )
}
