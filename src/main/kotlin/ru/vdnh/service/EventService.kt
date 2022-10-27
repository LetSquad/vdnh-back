package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.EventMapper
import ru.vdnh.model.dto.EventDTO
import ru.vdnh.repository.EventRepository
import java.math.BigInteger

@Service
class EventService(val eventRepository: EventRepository, val eventMapper: EventMapper) {
    fun findEvent(id: BigInteger): EventDTO {
        return eventRepository.findEvent(id)
            .let { eventMapper.entityToDomain(it) }
            .let { eventMapper.domainToDTO(it) }
    }

    fun getAll(): List<EventDTO> {
        return eventRepository.findAll()
            .map { eventMapper.entityToDomain(it) }
            .map { eventMapper.domainToDTO(it) }
    }
}