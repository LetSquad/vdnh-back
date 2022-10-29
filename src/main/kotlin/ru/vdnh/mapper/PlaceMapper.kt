package ru.vdnh.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import ru.vdnh.model.VdnhConstants.GEOMETRY_MAP_TYPE
import ru.vdnh.model.VdnhConstants.PLACE_MAP_TYPE
import ru.vdnh.model.domain.LocationCoordinates
import ru.vdnh.model.domain.LocationType
import ru.vdnh.model.domain.Place
import ru.vdnh.model.dto.GeometryDTO
import ru.vdnh.model.dto.PlaceDTO
import ru.vdnh.model.entity.PlaceEntity
import java.time.Duration

@Component
class PlaceMapper(
    private val mapper: ObjectMapper,
    private val propertyMapper: PropertyMapper
) {

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

        coordinates = Coordinates(
            id = placeEntity.coordinates.id,
            latitude = placeEntity.coordinates.latitude,
            longitude = placeEntity.coordinates.longitude,
            connections = listOf(), // TODO
            loadFactor = mapOf() // TODO
        ),

        type = LocationType(
            code = placeEntity.typeCode,
            name = placeEntity.type.name,
            nameEn = placeEntity.type.nameEn,
            nameCn = placeEntity.type.nameCn,
            iconCode = placeEntity.type.iconCode,
            iconColor = placeEntity.type.iconColor
        ),

        schedule = placeEntity.schedule?.let { mapper.readValue(it) },
        events = events.ifEmpty { null }
    )

    fun domainToDto(place: Place): PlaceDTO = PlaceDTO(
        id = place.id,
        type = PLACE_MAP_TYPE,
        geometry = GeometryDTO(
            type = GEOMETRY_MAP_TYPE,
            coordinates = listOf(place.coordinates.latitude, place.coordinates.longitude)
        ),

        property = propertyMapper.domainToDto(place)
    )
}
