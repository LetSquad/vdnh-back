package ru.vdnh.mapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import ru.vdnh.model.VdnhConstants.GEOMETRY_TYPE
import ru.vdnh.model.VdnhConstants.PLACE_TYPE
import ru.vdnh.model.domain.LocationCoordinates
import ru.vdnh.model.domain.LocationType
import ru.vdnh.model.domain.Place
import ru.vdnh.model.domain.Schedule
import ru.vdnh.model.dto.GeometryDTO
import ru.vdnh.model.dto.PlaceDTO
import ru.vdnh.model.entity.LocationTypeEntity
import ru.vdnh.model.entity.PlaceEntity
import ru.vdnh.model.enums.LocationPlacement
import ru.vdnh.model.enums.PaymentConditions
import java.sql.ResultSet
import java.time.Duration

@Component
class PlaceMapper(
    private val scheduleMapper: ScheduleMapper,
    private val coordinatesMapper: CoordinatesMapper,
    private val propertyMapper: PropertyMapper
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

        coordinates = coordinatesMapper.mapRow(rs, rowNum),

        schedule = scheduleMapper.mapRow(rs, rowNum),

        type = LocationTypeEntity(
            code = rs.getString("type_code"),
            name = rs.getString("name"),
            nameEn = rs.getString("name_en"),
            nameCn = rs.getString("name_cn"),
            iconCode = rs.getString("icon_code"),
            iconColor = rs.getString("icon_color")
        )
    )

    fun entityToDomain(placeEntity: PlaceEntity, events: List<Long>): Place = Place(
        id = placeEntity.id,
        title = placeEntity.title,
        titleEn = placeEntity.titleEn,
        titleCn = placeEntity.titleCn,
        priority = placeEntity.priority,
        url = placeEntity.url,
        imageUrl = placeEntity.imageUrl,
        ticketsUrl = placeEntity.ticketsUrl,
        isActive = placeEntity.isActive,
        createdAt = placeEntity.createdAt.toInstant(),
        typeCode = placeEntity.typeCode,
        subjectCode = placeEntity.subjectCode,
        paymentConditions = placeEntity.paymentConditions,
        placement = placeEntity.placement,
        visitTime = Duration.ofMinutes(placeEntity.visitTimeMinutes.toLong()),

        coordinates = LocationCoordinates(
            id = placeEntity.coordinates.id,
            latitude = placeEntity.coordinates.latitude,
            longitude = placeEntity.coordinates.longitude,
        ),

        type = LocationType(
            code = placeEntity.typeCode,
            name = placeEntity.type.name,
            nameEn = placeEntity.type.nameEn,
            nameCn = placeEntity.type.nameCn,
            iconCode = placeEntity.type.iconCode,
            iconColor = placeEntity.type.iconColor
        ),

        schedule = placeEntity.schedule?.let {
            Schedule(
                id = it.id,
                monday = placeEntity.schedule.monday,
                tuesday = placeEntity.schedule.tuesday,
                wednesday = placeEntity.schedule.wednesday,
                thursday = placeEntity.schedule.thursday,
                friday = placeEntity.schedule.friday,
                saturday = placeEntity.schedule.saturday,
                sunday = placeEntity.schedule.sunday,
                additionalInfo = placeEntity.schedule.additionalInfo,
            )
        },
        events = events.ifEmpty { null }
    )

    fun domainToDto(place: Place): PlaceDTO = PlaceDTO(
        id = place.id,
        type = PLACE_TYPE,
        geometry = GeometryDTO(
            type = GEOMETRY_TYPE,
            coordinates = listOf(place.coordinates.latitude, place.coordinates.longitude)
        ),

        property = propertyMapper.domainToDto(place)
    )
}
