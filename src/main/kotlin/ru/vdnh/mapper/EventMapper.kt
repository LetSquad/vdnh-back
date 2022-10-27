package ru.vdnh.mapper

import org.springframework.stereotype.Component
import ru.vdnh.model.domain.Event
import ru.vdnh.model.dto.EventDTO
import ru.vdnh.model.entity.EventEntity

@Component
class EventMapper {
    fun entityToDomain(entity: EventEntity) = Event(
        id = entity.id,
        title = entity.title,
        titleEn = entity.titleEn,
        titleCn = entity.titleCn,
        priority = entity.priority,
        url = entity.url,
        imageUrl = entity.imageUrl,
        coordinatesId = entity.coordinatesId,
        typeCode = entity.typeCode,
        subjectCode = entity.subjectCode
    )

    fun domainToDTO(domain: Event) = EventDTO(
        id = domain.id,
        title = domain.title,
        titleEn = domain.titleEn,
        titleCn = domain.titleCn,
        priority = domain.priority,
        url = domain.url,
        imageUrl = domain.imageUrl,
        coordinatesId = domain.coordinatesId,
        typeCode = domain.typeCode,
        subjectCode = domain.subjectCode
    )
}