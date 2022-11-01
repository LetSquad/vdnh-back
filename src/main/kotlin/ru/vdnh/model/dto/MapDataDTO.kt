package ru.vdnh.model.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Map data model")
data class MapDataDTO(val mapData: List<Any>)