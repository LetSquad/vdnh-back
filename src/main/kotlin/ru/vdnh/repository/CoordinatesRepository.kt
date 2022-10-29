package ru.vdnh.repository

import ru.vdnh.model.domain.LoadFactor
import ru.vdnh.model.entity.CoordinatesEntity

interface CoordinatesRepository {

    fun getAllCoordinates(): List<CoordinatesEntity>

    fun getLoadFactorByCoordinateId(id: Long): LoadFactor
}
