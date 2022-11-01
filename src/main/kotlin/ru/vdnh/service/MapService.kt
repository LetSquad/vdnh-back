package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.getLogger
import ru.vdnh.mapper.EventMapper
import ru.vdnh.mapper.PlaceMapper
import ru.vdnh.model.dto.MapDataDTO

@Service
class MapService(
    private val eventService: EventService,
    private val eventMapper: EventMapper,
    private val placesService: PlacesService,
    private val placeMapper: PlaceMapper
) {

    fun getEventsAndPlaces(): MapDataDTO {
        val events = eventService.getAllEvents()
            .map { eventMapper.domainToDTO(it) }

        val places = placesService.getAllPlaces()
            .map { placeMapper.domainToDto(it) }

        return MapDataDTO(listOf(places, events))
    }

    companion object {
        private val log = getLogger<MapService>()
    }
}
