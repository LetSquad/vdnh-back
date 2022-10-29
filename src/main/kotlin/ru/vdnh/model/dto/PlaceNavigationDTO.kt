package ru.vdnh.model.dto

import java.math.BigInteger

data class PlaceNavigationDTO(
    val startCoordinateId: BigInteger?,
    val finishCoordinatesId: BigInteger?,
)
