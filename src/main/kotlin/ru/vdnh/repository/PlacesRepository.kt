package ru.vdnh.repository

import ru.vdnh.model.entity.PlaceEntity
import java.math.BigInteger

interface PlacesRepository {

    fun getAllPlaces(): List<PlaceEntity>

    fun getPlaceById(id: BigInteger): PlaceEntity

    fun getEventsByPlaceId(placeId: Long): List<Long>
}
