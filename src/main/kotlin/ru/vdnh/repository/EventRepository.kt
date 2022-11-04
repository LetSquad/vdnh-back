package ru.vdnh.repository

import ru.vdnh.model.entity.EventEntity

interface EventRepository {
    fun findEvent(id: Long): EventEntity

    fun getAllEvents(): List<EventEntity>

    fun getAllActiveEvents(): List<EventEntity>

    fun getPlacesByEventId(eventId: Long): List<Long>

    fun getAllActiveWhereSubjectCode(subjectCode: String): List<EventEntity>

    fun getByCoordinatesId(coordinatesId: Long): EventEntity
}
