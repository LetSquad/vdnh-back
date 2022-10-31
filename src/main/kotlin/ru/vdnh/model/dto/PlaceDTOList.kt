package ru.vdnh.model.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Place list model")
data class PlaceDTOList(val places: List<Any>)