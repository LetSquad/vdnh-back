package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.EventMapper
import ru.vdnh.mapper.PlaceMapper
import ru.vdnh.model.dto.PlaceXEventDTOList

@Service
class MapService(
    private val eventService: EventService,
    private val eventMapper: EventMapper,
    private val placesService: PlacesService,
    private val placeMapper: PlaceMapper
) {

    fun getEventsAndPlaces(): PlaceXEventDTOList {
        val events = eventService.getAllEvents()
            .map { eventMapper.domainToDTO(it) }

        val places = placesService.getAllPlaces()
            .map { placeMapper.domainToDto(it) }

        return PlaceXEventDTOList(listOf(places, events))
    }
}
