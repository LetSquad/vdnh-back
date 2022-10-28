package ru.vdnh.repository

import ru.vdnh.model.entity.CoordinatesEntity

interface CoordinatesRepository {

    fun getAllCoordinates(): List<CoordinatesEntity>
}
