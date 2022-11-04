package ru.vdnh.repository

import ru.vdnh.model.entity.PlaceEntity
import java.math.BigInteger

interface PlaceRepository {

    fun getAllActivePlaces(): List<PlaceEntity>

    fun getPlaceById(id: BigInteger): PlaceEntity

    fun getEventsByPlaceId(placeId: Long): List<Long>

    fun getAllActiveWhereSubject(subjectCode: String): List<PlaceEntity>

    fun getByCoordinatesId(coordinatesId: Long): PlaceEntity

    fun getPlacesByRouteId(id: BigInteger): List<PlaceEntity>
}
