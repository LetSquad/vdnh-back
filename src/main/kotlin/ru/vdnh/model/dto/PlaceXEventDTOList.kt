package ru.vdnh.model.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "PlaceXEvent list model")
data class PlaceXEventDTOList(val placesXEvents: List<Any>)