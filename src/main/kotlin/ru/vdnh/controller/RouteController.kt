package ru.vdnh.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.vdnh.model.dto.RouteDTOList
import ru.vdnh.service.RouteService
import java.math.BigInteger

@RestController
@RequestMapping("route")
@Tag(name = "Методы работы с маршрутами")
class RouteController(val routeService: RouteService) {
    @Operation(
        summary = "Получение готового маршрута",
        description = "Получение готового маршрута по идентификаторам места"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation")
        ]
    )
    @GetMapping
    fun findRoutes(@RequestParam idFrom: BigInteger, @RequestParam idTo: BigInteger): RouteDTOList {
        return routeService.findRoutes(idFrom, idTo)
    }
}