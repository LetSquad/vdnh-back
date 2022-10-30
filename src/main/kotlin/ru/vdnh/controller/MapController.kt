package ru.vdnh.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.vdnh.model.dto.HeatmapDTO
import ru.vdnh.service.CoordinatesService
import java.time.DayOfWeek

@Tag(name = "Методы работы с картой")
@RestController
@RequestMapping("/map")
class MapController(private val coordinatesService: CoordinatesService) {

    @Operation(
        summary = "Получение тепловой карты",
        description = "Получение тепловой карты по дню недели и времени"
    )
    @GetMapping("/heatmap")
    fun getHeatmap(@RequestParam day: DayOfWeek, time: String): HeatmapDTO {
        return coordinatesService.getHeatmap(day, time)
    }
}
