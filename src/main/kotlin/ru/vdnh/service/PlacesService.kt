package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.EventMapper
import ru.vdnh.mapper.PlaceMapper
import ru.vdnh.model.dto.PlaceDTO
import ru.vdnh.model.dto.PlaceDTOList
import ru.vdnh.repository.EventRepository
import ru.vdnh.repository.PlacesRepository
import java.math.BigInteger

@Service
class PlacesService(
    private val placesRepository: PlacesRepository,
    private val placeMapper: PlaceMapper,
    private val eventRepository: EventRepository,
    private val eventMapper: EventMapper
) {

    fun getAll(): PlaceDTOList {
        val places = placesRepository.getAllPlaces()
        val mergedPlacesEventsList = mutableListOf<Any>()
        for (place in places) {
            val eventsByPlaceId = placesRepository.getEventsByPlaceId(place.id)
            val entityToDomain = placeMapper.entityToDomain(place, eventsByPlaceId)
            mergedPlacesEventsList.add(placeMapper.domainToDto(entityToDomain))
        }

        val events = eventRepository.getAllEvents()
        for (event in events) {
            val placesByEventId = eventRepository.getPlacesByEventId(event.id)
            val entityToDomain = eventMapper.entityToDomain(event, placesByEventId)
            mergedPlacesEventsList.add(eventMapper.domainToDTO(entityToDomain))
        }


        return PlaceDTOList(mergedPlacesEventsList)
    }

    fun getPlaceById(id: BigInteger): PlaceDTO {
        val place = placesRepository.getPlaceById(id)
        val eventsByPlaceId = placesRepository.getEventsByPlaceId(place.id)
        return place
            .let { placeMapper.entityToDomain(it, eventsByPlaceId) }
            .let { placeMapper.domainToDto(it) }
    }
}
