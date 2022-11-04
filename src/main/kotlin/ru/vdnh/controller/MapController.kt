package ru.vdnh.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.vdnh.model.dto.HeatmapDTO
import ru.vdnh.model.dto.MapDataDTO
import ru.vdnh.service.CoordinatesService
import ru.vdnh.service.MapService

@Tag(name = "Методы работы с картой")
@RestController
@RequestMapping("/map")
class MapController(
    private val coordinatesService: CoordinatesService,
    private val mapService: MapService
) {

    @Operation(
        summary = "Получение тепловой карты",
        description = "Получение тепловой карты по дню недели и времени"
    )
    @GetMapping("/heatmap")
    fun getHeatmap(@RequestParam dayNumber: Int?, @RequestParam time: String?): HeatmapDTO {
        return coordinatesService.getHeatmap(dayNumber, time)
    }

    @Operation(
        summary = "Получение событий и мест",
        description = "Получение событий и мест в определенном формате для отображения на карте"
    )
    @GetMapping("/data")
    fun getEventsAndPlaces(): MapDataDTO {
        return mapService.getEventsAndPlaces()
    }

}
