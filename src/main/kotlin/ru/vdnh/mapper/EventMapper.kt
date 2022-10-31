package ru.vdnh.mapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import ru.vdnh.model.VdnhConstants.GEOMETRY_TYPE
import ru.vdnh.model.VdnhConstants.PLACE_TYPE
import ru.vdnh.model.domain.Event
import ru.vdnh.model.domain.LocationType
import ru.vdnh.model.dto.EventDTO
import ru.vdnh.model.dto.GeometryDTO
import ru.vdnh.model.entity.EventEntity
import ru.vdnh.model.entity.LocationTypeEntity
import ru.vdnh.model.enums.LocationPlacement
import ru.vdnh.model.enums.PaymentConditions
import java.sql.ResultSet
import java.time.Duration

@Component
class EventMapper(
    private val coordinatesMapper: CoordinatesMapper,
    private val scheduleMapper: ScheduleMapper,
    private val propertyMapper: PropertyMapper
) : RowMapper<EventEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): EventEntity = EventEntity(
        id = rs.getLong("id"),
        title = rs.getString("title"),
        titleEn = rs.getString("title_en"),
        titleCn = rs.getString("title_cn"),
        priority = rs.getInt("priority"),
        url = rs.getString("url"),
        imageUrl = rs.getString("image_url"),
        isActive = rs.getBoolean("is_active"),
        createdAt = rs.getTimestamp("created_at"),
        visitTimeMinutes = rs.getInt("visit_time_minutes"),
        typeCode = rs.getString("type_code"),
        subjectCode = rs.getString("subject_code"),
        paymentConditions = PaymentConditions.valueOf(rs.getString("payment_conditions")),
        placement = LocationPlacement.valueOf(rs.getString("placement")),

        coordinates = coordinatesMapper.mapRow(rs, rowNum),
        schedule = scheduleMapper.mapRow(rs, rowNum),

        startDate = rs.getDate("start_date"),
        finishDate = rs.getDate("finish_date"),

        type = LocationTypeEntity(
            code = rs.getString("type_code"),
            name = rs.getString("name"),
            nameEn = rs.getString("name_en"),
            nameCn = rs.getString("name_cn"),
            iconCode = rs.getString("icon_code"),
            iconColor = rs.getString("icon_color")
        )
    )

    fun entityToDomain(entity: EventEntity, places: List<Long>) = Event(
        id = entity.id,
        title = entity.title,
        titleEn = entity.titleEn,
        titleCn = entity.titleCn,
        priority = entity.priority,
        visitTime = Duration.ofMinutes(entity.visitTimeMinutes.toLong()),
        placement = entity.placement,
        paymentConditions = entity.paymentConditions,
        url = entity.url,
        imageUrl = entity.imageUrl,
        isActive = entity.isActive,
        startDate = entity.startDate,
        finishDate = entity.finishDate,
        typeCode = entity.typeCode,
        subjectCode = entity.subjectCode,
        coordinates = entity.coordinates?.let { coordinatesMapper.entityToLocationDomain(it) },
        schedule = null,
        createdAt = entity.createdAt.toInstant(),
        type = LocationType(
            code = entity.typeCode,
            name = entity.type.name,
            nameEn = entity.type.nameEn,
            nameCn = entity.type.nameCn,
            iconCode = entity.type.iconCode,
            iconColor = entity.type.iconColor
        ),
        places = places.ifEmpty { null }
    )

    fun domainToDTO(event: Event) = EventDTO(
        id = event.id,
        type = PLACE_TYPE,
        geometry = GeometryDTO(
            type = GEOMETRY_TYPE,
            coordinates = listOf(event.coordinates?.latitude, event.coordinates?.longitude)
        ),

        property = propertyMapper.domainToDto(event)
    )
}
