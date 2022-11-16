package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.PlaceMapper
import ru.vdnh.model.domain.Place
import ru.vdnh.model.domain.RoutePlace
import ru.vdnh.model.dto.PlaceDTO
import ru.vdnh.model.entity.PlaceEntity
import ru.vdnh.repository.PlaceRepository

@Service
class PlaceService(
    private val placeRepository: PlaceRepository,
    private val placeMapper: PlaceMapper
) {

    fun getAllActivePlaces(): List<Place> {
        val placeEntities: List<PlaceEntity> = placeRepository.getAllActivePlaces()
        val placeDomainList = mutableListOf<Place>()
        for (place in placeEntities) {
            val eventsByPlaceId: List<Long> = placeRepository.getEventsByPlaceId(place.id)
            placeDomainList.add(placeMapper.entityToDomain(place, eventsByPlaceId))
        }
        return placeDomainList
    }

    fun getPlacesByType(typeCode: String): List<Place> {
        val placeEntities: List<PlaceEntity> = placeRepository.getPlacesByType(typeCode)
        val placeDomainList = mutableListOf<Place>()
        for (place in placeEntities) {
            val eventsByPlaceId: List<Long> = placeRepository.getEventsByPlaceId(place.id)
            placeDomainList.add(placeMapper.entityToDomain(place, eventsByPlaceId))
        }
        return placeDomainList
    }

    fun getPlaceDTOById(id: Long): PlaceDTO {
        val place = placeRepository.getPlaceById(id)
        val eventsByPlaceId = placeRepository.getEventsByPlaceId(place.id)
        return place
            .let { placeMapper.entityToDomain(it, eventsByPlaceId) }
            .let { placeMapper.domainToDto(it) }
    }

    fun getPlaceById(id: Long): Place {
        val place = placeRepository.getPlaceById(id)
        val eventsByPlaceId = placeRepository.getEventsByPlaceId(place.id)
        return place
            .let { placeMapper.entityToDomain(it, eventsByPlaceId) }
    }

    fun getPlaceByCoordinateId(coordinateId: Long): Place {
        val place = placeRepository.getPlaceByCoordinateId(coordinateId)
        val eventsByPlaceId = placeRepository.getEventsByPlaceId(place.id)
        return place
            .let { placeMapper.entityToDomain(it, eventsByPlaceId) }
    }

    fun getActivePlacesBySubject(subjectCode: String): List<Place> {
        val placeEntities: List<PlaceEntity> = placeRepository.getAllActiveWhereSubject(subjectCode)
        val placeDomainList = mutableListOf<Place>()
        for (place in placeEntities) {
            val eventsByPlaceId: List<Long> = placeRepository.getEventsByPlaceId(place.id)
            placeDomainList.add(placeMapper.entityToDomain(place, eventsByPlaceId))
        }
        return placeDomainList
    }

    fun getPlacesByRouteId(routeId: Long, routePlace: RoutePlace): Place {
        val place = placeRepository.getPlaceById(routePlace.placeId)

        val eventsByPlaceId: List<Long> = placeRepository.getEventsByPlaceId(place.id)
        return placeMapper.entityToDomain(place, eventsByPlaceId)
    }
}
