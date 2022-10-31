package ru.vdnh.mapper

import org.springframework.stereotype.Component
import ru.vdnh.model.domain.Place
import ru.vdnh.model.entity.PlaceEntity
import java.time.Duration


@Component
class PlaceMapper(
    private val coordinatesMapper: CoordinatesMapper
) {

    fun entityToDomain(entity: PlaceEntity) = Place(
        id = entity.id,
        title = entity.title,
        titleEn = entity.titleEn,
        titleCn = entity.titleCn,
        priority = entity.priority,
        url = entity.url,
        imageUrl = entity.imageUrl,
        ticketsUrl = entity.ticketsUrl,
        isActive = entity.isActive,
        coordinates = entity.coordinates.let { coordinatesMapper.entityToLocationDomain(it) },
        schedule = null,
        paymentConditions = entity.paymentConditions,
        placement = entity.placement,
        visitTime = Duration.ofMinutes(entity.visitTimeMinutes.toLong()),
        typeCode = entity.typeCode,
        subjectCode = entity.subjectCode,
        createdAt = entity.createdAt.toInstant()
    )

}
