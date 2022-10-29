package ru.vdnh.repository

import ru.vdnh.model.entity.PlaceEntity
import java.math.BigInteger

interface PlaceRepository {

    fun getAllPlaces(): List<PlaceEntity>

    fun getPlaceById(id: BigInteger): PlaceEntity

    fun getEventsByPlaceId(placeId: Long): List<Long>

    fun getAllActiveWhereSubject(subjectCode: String): List<PlaceEntity>

    fun getByCoordinatesId(coordinatesId: Long): PlaceEntity

    fun existById(id: Long): Boolean
}
