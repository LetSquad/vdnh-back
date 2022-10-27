package ru.vdnh.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.vdnh.model.dto.EventDTO
import ru.vdnh.service.EventService
import java.math.BigInteger

@RestController
@RequestMapping("event")
@Tag(name = "Методы работы с событиями")
class EventController(val eventService: EventService) {
    @Operation(
        summary = "Получение события по его идентификатору"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation")
        ]
    )
    @GetMapping("{id}")
    fun findEvent(@PathVariable id: BigInteger): EventDTO {
        return eventService.findEvent(id)
    }

    @Operation(
        summary = "Получение всех событий"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation")
        ]
    )
    @GetMapping
    fun getAll(): List<EventDTO> {
        return eventService.getAll()
    }
}