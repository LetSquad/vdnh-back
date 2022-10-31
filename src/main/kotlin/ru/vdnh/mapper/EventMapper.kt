package ru.vdnh.mapper

import org.springframework.stereotype.Component
import ru.vdnh.model.domain.Event
import ru.vdnh.model.dto.EventDTO
import ru.vdnh.model.entity.EventEntity
import java.time.Duration

@Component
class EventMapper(private val coordinatesMapper: CoordinatesMapper) {

    fun entityToDomain(entity: EventEntity) = Event(
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
        createdAt = entity.createdAt.toInstant()
    )

    fun domainToDTO(domain: Event) = EventDTO(
        id = domain.id,
        title = domain.title,
        titleEn = domain.titleEn,
        titleCn = domain.titleCn,
        priority = domain.priority,
        url = domain.url,
        imageUrl = domain.imageUrl,
        coordinatesId = domain.coordinates?.id,
        typeCode = domain.typeCode,
        subjectCode = domain.subjectCode
    )
}
