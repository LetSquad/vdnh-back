package ru.vdnh.model.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Event list model")
data class EventDTOList(val events: List<EventDTO>)