package ru.vdnh.model.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Route list model")
data class RouteDTOList(val routeDTOList: List<RouteDTO>)