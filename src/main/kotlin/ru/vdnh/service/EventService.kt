package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.EventMapper
import ru.vdnh.model.domain.Event
import ru.vdnh.model.domain.Place
import ru.vdnh.model.dto.EventDTO
import ru.vdnh.model.entity.EventEntity
import ru.vdnh.repository.EventRepository
import java.math.BigInteger

@Service
class EventService(
    val eventRepository: EventRepository,
    val eventMapper: EventMapper,

    val placeService: PlaceService
) {

    fun getAllEvents(): List<Event> {
        val events: List<EventEntity> = eventRepository.getAllEvents()
        val eventsDomainList = mutableListOf<Event>()
        for (event in events) {
            val placesIdByEventId: List<Long> = eventRepository.getPlacesByEventId(event.id)
            val placesByEventId: List<Place> = placesIdByEventId
                .map { placeService.getPlaceById(BigInteger.valueOf(it)) }

                eventsDomainList.add(eventMapper.entityToDomain(event, placesByEventId))
        }
        return eventsDomainList
    }

    fun findEvent(id: BigInteger): EventDTO {
        val event = eventRepository.findEvent(id)
        val placesIdByEventId: List<Long> = eventRepository.getPlacesByEventId(event.id)
        val placesByEventId: List<Place> = placesIdByEventId
            .map { placeService.getPlaceById(BigInteger.valueOf(it)) }

        return event
            .let { eventMapper.entityToDomain(it, placesByEventId) }
            .let { eventMapper.domainToDTO(it) }
    }

    fun getByCoordinatesId(coordinatesId: Long): EventDTO {
        val event = eventRepository.getByCoordinatesId(coordinatesId)
        val placesIdByEventId: List<Long> = eventRepository.getPlacesByEventId(event.id)
        val placesByEventId: List<Place> = placesIdByEventId
            .map { placeService.getPlaceById(BigInteger.valueOf(it)) }

        return event
            .let { eventMapper.entityToDomain(it, placesByEventId) }
            .let { eventMapper.domainToDTO(it) }
    }

    fun getActiveEventsBySubject(subjectCode: String): List<Event> {
        val events: List<EventEntity> = eventRepository.getAllActiveWhereSubjectCode(subjectCode)
        val eventsDomainList = mutableListOf<Event>()
        for (event in events) {
            val placesIdByEventId: List<Long> = eventRepository.getPlacesByEventId(event.id)
            val placesByEventId: List<Place> = placesIdByEventId
                .map { placeService.getPlaceById(BigInteger.valueOf(it)) }
            eventsDomainList.add(eventMapper.entityToDomain(event, placesByEventId))
        }
        return eventsDomainList
    }

}
