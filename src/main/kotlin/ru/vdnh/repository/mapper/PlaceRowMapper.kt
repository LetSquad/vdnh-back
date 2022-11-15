package ru.vdnh.repository.mapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import ru.vdnh.model.entity.LocationTypeEntity
import ru.vdnh.model.entity.PlaceEntity
import ru.vdnh.model.enums.LocationPlacement
import ru.vdnh.model.enums.PaymentConditions
import java.sql.ResultSet

@Component
class PlaceRowMapper(
    private val coordinatesRowMapper: CoordinatesRowMapper
) : RowMapper<PlaceEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): PlaceEntity = PlaceEntity(
        id = rs.getLong("id"),
        title = rs.getString("title"),
        titleEn = rs.getString("title_en"),
        titleCn = rs.getString("title_cn"),
        priority = rs.getInt("priority"),
        url = rs.getString("url"),
        imageUrl = rs.getString("image_url"),
        ticketsUrl = rs.getString("tickets_url"),
        isActive = rs.getBoolean("is_active"),
        createdAt = rs.getTimestamp("created_at"),
        visitTimeMinutes = rs.getInt("visit_time_minutes"),
        typeCode = rs.getString("type_code"),
        subjectCode = rs.getString("subject_code"),
        paymentConditions = PaymentConditions.valueOf(rs.getString("payment_conditions")),
        placement = LocationPlacement.valueOf(rs.getString("placement")),
        schedule = rs.getString("schedule"),

        coordinates = coordinatesRowMapper.mapRow(rs, rowNum),

        type = LocationTypeEntity(
            code = rs.getString("type_code"),
            name = rs.getString("name"),
            nameEn = rs.getString("name_en"),
            nameCn = rs.getString("name_cn"),
            iconCode = rs.getString("icon_code"),
            iconColor = rs.getString("icon_color")
        ),

        description = rs.getString("description"),
        descriptionEn = rs.getString("description_en"),
        descriptionCn = rs.getString("description_cn")
    )
}
