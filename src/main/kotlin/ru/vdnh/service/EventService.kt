package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.EventMapper
import ru.vdnh.model.dto.EventDTO
import ru.vdnh.repository.EventRepository
import java.math.BigInteger

@Service
class EventService(val eventRepository: EventRepository, val eventMapper: EventMapper) {
    fun findEvent(id: BigInteger): EventDTO {
        val event = eventRepository.findEvent(id)
        val places = eventRepository.getPlacesByEventId(event.id)
        return event
            .let { eventMapper.entityToDomain(it, places) }
            .let { eventMapper.domainToDTO(it) }
    }
}