package ru.vdnh.mapper

import org.springframework.stereotype.Component
import ru.vdnh.model.domain.Place
import ru.vdnh.model.entity.PlaceEntity


@Component
class PlaceMapper {

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
        coordinatesId = entity.coordinatesId,
        scheduleId = entity.scheduleId,
        typeCode = entity.typeCode,
        subjectCode = entity.subjectCode
    )

}
