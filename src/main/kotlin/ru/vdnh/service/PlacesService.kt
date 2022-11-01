package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.PlaceMapper
import ru.vdnh.model.domain.Place
import ru.vdnh.model.dto.PlaceDTO
import ru.vdnh.model.entity.PlaceEntity
import ru.vdnh.repository.PlacesRepository
import java.math.BigInteger

@Service
class PlacesService(
    private val placesRepository: PlacesRepository,
    private val placeMapper: PlaceMapper
) {

    fun getAllPlaces(): List<Place> {
        val placeEntities: List<PlaceEntity> = placesRepository.getAllPlaces()
        val placeDomainList = mutableListOf<Place>()
        for (place in placeEntities) {
            val eventsByPlaceId: List<Long> = placesRepository.getEventsByPlaceId(place.id)
            placeDomainList.add(placeMapper.entityToDomain(place, eventsByPlaceId))
        }
        return placeDomainList
    }

    fun getPlaceById(id: BigInteger): PlaceDTO {
        val place = placesRepository.getPlaceById(id)
        val eventsByPlaceId = placesRepository.getEventsByPlaceId(place.id)
        return place
            .let { placeMapper.entityToDomain(it, eventsByPlaceId) }
            .let { placeMapper.domainToDto(it) }
    }
}
