package ru.vdnh.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.vdnh.model.dto.PlaceDTO
import ru.vdnh.service.PlacesService
import java.math.BigInteger

@Tag(name = "Методы работы с местами")
@RestController
@RequestMapping("/place")
class PlaceController(private val placesService: PlacesService) {

    @Operation(
        summary = "Получение места по его идентификатору"
    )
    @GetMapping("{id}")
    fun findEvent(@PathVariable id: BigInteger): PlaceDTO {
        return placesService.getPlaceById(id)
    }
}
