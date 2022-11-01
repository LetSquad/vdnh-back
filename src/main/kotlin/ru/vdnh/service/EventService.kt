package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.EventMapper
import ru.vdnh.model.domain.Event
import ru.vdnh.model.dto.EventDTO
import ru.vdnh.model.entity.EventEntity
import ru.vdnh.repository.EventRepository
import java.math.BigInteger

@Service
class EventService(val eventRepository: EventRepository, val eventMapper: EventMapper) {

    fun getAllEvents(): List<Event> {
        val events: List<EventEntity> = eventRepository.getAllEvents()
        val eventsDomainList = mutableListOf<Event>()
        for (event in events) {
            val placesByEventId: List<Long> = eventRepository.getPlacesByEventId(event.id)
            eventsDomainList.add(eventMapper.entityToDomain(event, placesByEventId))
        }
        return eventsDomainList
    }

    fun findEvent(id: BigInteger): EventDTO {
        val event = eventRepository.findEvent(id)
        val places = eventRepository.getPlacesByEventId(event.id)
        return event
            .let { eventMapper.entityToDomain(it, places) }
            .let { eventMapper.domainToDTO(it) }
    }
}