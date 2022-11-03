package ru.vdnh.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.vdnh.model.dto.FastNavigationRequestDTO
import ru.vdnh.model.dto.HeatmapDTO
import ru.vdnh.model.dto.MapDataDTO
import ru.vdnh.model.dto.PlaceDTOList
import ru.vdnh.service.CoordinatesService
import ru.vdnh.service.MapService
import ru.vdnh.service.NavigationService
import ru.vdnh.service.RouteService
import java.math.BigInteger

@Tag(name = "Методы работы с картой")
@RestController
@RequestMapping("/map")
class MapController(
    private val coordinatesService: CoordinatesService,
    private val navigationService: NavigationService,
    private val mapService: MapService,
    private val routeService: RouteService
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
        summary = "Получение готового маршрута",
        description = "Получение готового маршрута по его идентификатору"
    )
    @GetMapping("/route")
    fun getRouteById(@RequestParam id: BigInteger): PlaceDTOList {
        return routeService.getRoute(id)
    }

    @Operation(
        summary = "Получение событий и мест",
        description = "Получение событий и мест в определенном формате для отображения на карте"
    )
    @GetMapping("/data")
    fun getEventsAndPlaces(): MapDataDTO {
        return mapService.getEventsAndPlaces()
    }

    @Operation(
        summary = "Построение быстрого маршрута"
    )
    @PostMapping("fast")
    fun fastNavigate(@RequestBody dto: FastNavigationRequestDTO): MapDataDTO {
        return navigationService.fastNavigate(dto)
    }
}
