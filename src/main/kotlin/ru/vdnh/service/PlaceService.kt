package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.PlaceMapper
import ru.vdnh.model.domain.Place
import ru.vdnh.model.dto.PlaceDTO
import ru.vdnh.model.entity.PlaceEntity
import ru.vdnh.repository.PlaceRepository
import java.math.BigInteger

@Service
class PlaceService(
    private val placeRepository: PlaceRepository,
    private val placeMapper: PlaceMapper
) {

    fun getAllPlaces(): List<Place> {
        val placeEntities: List<PlaceEntity> = placeRepository.getAllPlaces()
        val placeDomainList = mutableListOf<Place>()
        for (place in placeEntities) {
            val eventsByPlaceId: List<Long> = placeRepository.getEventsByPlaceId(place.id)
            placeDomainList.add(placeMapper.entityToDomain(place, eventsByPlaceId))
        }
        return placeDomainList
    }

    fun getPlaceDTOById(id: BigInteger): PlaceDTO {
        val place = placeRepository.getPlaceById(id)
        val eventsByPlaceId = placeRepository.getEventsByPlaceId(place.id)
        return place
            .let { placeMapper.entityToDomain(it, eventsByPlaceId) }
            .let { placeMapper.domainToDto(it) }
    }

    fun getPlaceById(id: BigInteger): Place {
        val place = placeRepository.getPlaceById(id)
        val eventsByPlaceId = placeRepository.getEventsByPlaceId(place.id)
        return place
            .let { placeMapper.entityToDomain(it, eventsByPlaceId) }
    }

    fun getActivePlacesBySubject(subjectCode: String): List<Place> {
        val placeEntities: List<PlaceEntity> = placesRepository.getAllActiveWhereSubject(subjectCode)
        val placeDomainList = mutableListOf<Place>()
        for (place in placeEntities) {
            val eventsByPlaceId: List<Long> = placesRepository.getEventsByPlaceId(place.id)
            placeDomainList.add(placeMapper.entityToDomain(place, eventsByPlaceId))
        }
        return placeDomainList
    }

    fun getByCoordinatesId(coordinatesId: Long): Place {
        val place = placesRepository.getByCoordinatesId(coordinatesId)
        val eventsByPlaceId = placesRepository.getEventsByPlaceId(place.id)
        return place
            .let { placeMapper.entityToDomain(it, eventsByPlaceId) }
    }

    fun exist(coordinatesId: Long): Boolean =
        placesRepository.existById(coordinatesId)

}
