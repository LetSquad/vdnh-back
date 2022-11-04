package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.EventMapper
import ru.vdnh.mapper.PlaceMapper
import ru.vdnh.model.dto.MapDataDTO

@Service
class MapService(
    private val eventService: EventService,
    private val eventMapper: EventMapper,
    private val placeService: PlaceService,
    private val placeMapper: PlaceMapper
) {

    fun getEventsAndPlaces(): MapDataDTO {
        val events = eventService.getAllEvents()
            .filter { it.coordinates != null }
            .map { eventMapper.domainToDTO(it) }

        val places = placeService.getAllActivePlaces()
            .map { placeMapper.domainToDto(it) }

        return MapDataDTO(places + events)
    }
}
