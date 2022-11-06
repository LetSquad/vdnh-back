package ru.vdnh.repository

import ru.vdnh.model.entity.PlaceEntity

interface PlaceRepository {

    fun getAllActivePlaces(): List<PlaceEntity>

    fun getPlaceById(id: Long): PlaceEntity

    fun getPlaceByCoordinateId(coordinateId: Long): PlaceEntity

    fun getEventsByPlaceId(placeId: Long): List<Long>

    fun getAllActiveWhereSubject(subjectCode: String): List<PlaceEntity>

    fun getByCoordinatesId(coordinatesId: Long): PlaceEntity

    fun getPlacesByRouteId(id: Long): List<PlaceEntity>

    fun getPlacesByType(typeCode: String): List<PlaceEntity>
}
