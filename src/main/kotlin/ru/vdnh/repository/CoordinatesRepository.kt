package ru.vdnh.repository

import ru.vdnh.model.entity.CoordinatesEntity
import java.math.BigInteger

interface CoordinatesRepository {

    fun getAllCoordinates(): List<CoordinatesEntity>

    fun getCoordinatesById(id: BigInteger): CoordinatesEntity

    fun get(id: Long): CoordinatesEntity?
}
