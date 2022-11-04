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
import ru.vdnh.model.dto.MapRouteDataDTO
import ru.vdnh.service.RouteService


@Tag(name = "Методы работы с маршрутами")
@RestController
@RequestMapping("/route")
class RouteController(
    private val routeService: RouteService
) {

    @Operation(
        summary = "Получение готового маршрута",
        description = "Получение готового маршрута по его идентификатору"
    )
    @GetMapping("/prepared")
    fun getPreparedRoute(@RequestParam id: Long): MapRouteDataDTO {
        return routeService.getPreparedRoute(id)
    }

    @Operation(
        summary = "Построение быстрого маршрута по критериям"
    )
    @PostMapping("navigate")
    fun getNavigateRoute(@RequestBody dto: FastNavigationRequestDTO): MapRouteDataDTO {
        return routeService.getNavigateRoute(dto)
    }
}
