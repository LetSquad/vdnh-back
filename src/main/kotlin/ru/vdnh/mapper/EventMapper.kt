package ru.vdnh.mapper

import org.springframework.stereotype.Component
import ru.vdnh.model.VdnhConstants.GEOMETRY_MAP_TYPE
import ru.vdnh.model.VdnhConstants.PLACE_MAP_TYPE
import ru.vdnh.model.domain.Event
import ru.vdnh.model.domain.LocationType
import ru.vdnh.model.dto.EventDTO
import ru.vdnh.model.dto.GeometryDTO
import ru.vdnh.model.entity.EventEntity
import java.time.Duration

@Component
class EventMapper(
    private val coordinatesMapper: CoordinatesMapper,
    private val propertyMapper: PropertyMapper
) {

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
        type = PLACE_MAP_TYPE,
        geometry = GeometryDTO(
            type = GEOMETRY_MAP_TYPE,
            coordinates = listOf(event.coordinates?.latitude, event.coordinates?.longitude)
        ),

        property = propertyMapper.domainToDto(event)
    )
}
