package ru.vdnh.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.vdnh.model.dto.CoordinateDTOList
import ru.vdnh.model.dto.HeatmapDTO
import ru.vdnh.model.dto.PlaceXEventDTOList
import ru.vdnh.service.CoordinatesService
import ru.vdnh.service.MapService
import java.math.BigInteger
import java.time.DayOfWeek

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
    fun getHeatmap(@RequestParam day: DayOfWeek, time: String): HeatmapDTO {
        return coordinatesService.getHeatmap(day, time)
    }

    @Operation(
        summary = "Получение готового маршрута",
        description = "Получение готового маршрута по идентификаторам места"
    )
    @GetMapping("/route")
    fun findRoutes(@RequestParam idFrom: BigInteger, @RequestParam idTo: BigInteger): CoordinateDTOList {
        return coordinatesService.findRoutes(idFrom, idTo)
    }

    @Operation(
        summary = "Получение событий и мест",
        description = "Получение событий и мест в определенном формате для отображения на карте"
    )
    @GetMapping("/eventsXplaces")
    fun getEventsAndPlaces(): PlaceXEventDTOList {
        return mapService.getEventsAndPlaces()
    }
}
