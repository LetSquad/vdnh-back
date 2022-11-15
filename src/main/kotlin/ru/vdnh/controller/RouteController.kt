package ru.vdnh.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.vdnh.model.dto.MapRouteDataDTO
import ru.vdnh.model.dto.PreparedRouteDataDTO
import ru.vdnh.model.dto.PreparedRouteNavigationDTO
import ru.vdnh.model.dto.RouteNavigationDTO
import ru.vdnh.service.RouteService


@Tag(name = "Методы работы с маршрутами")
@RestController
@RequestMapping("/route")
class RouteController(
    private val routeService: RouteService
) {

    @Operation(
        summary = "Получение готового маршрута",
        description = "Получение готового маршрута"
    )
    @PostMapping("/prepared")
    fun getPreparedRoute(@RequestBody dto: PreparedRouteNavigationDTO): PreparedRouteDataDTO {
        return routeService.getPreparedRoute(dto)
    }

    @Operation(
        summary = "Построение маршрута по критериям"
    )
    @PostMapping("navigate")
    fun getNavigateRoute(@RequestBody dto: RouteNavigationDTO): MapRouteDataDTO {
        return routeService.getNavigateRoute(dto)
    }
}
