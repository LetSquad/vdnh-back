package ru.vdnh.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import ru.vdnh.model.dto.CoordinatesDto
import ru.vdnh.model.dto.NavigateDto
import ru.vdnh.service.NavigationService

@RestController
@RequestMapping("navigate")
@Tag(name = "Методы построения маршрута")
class NavigationController(
    private val navigationService: NavigationService
) {

    @Operation(
        summary = "Построение маршрута по тематикам"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation")
        ]
    )
    @PostMapping("by_subjects")
    fun navigateBySubjectsAndLoadFactors(@RequestBody dto: NavigateDto): List<List<CoordinatesDto>> =
        navigationService.getCoordinatesListBySubjects(dto)

}
