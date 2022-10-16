package ru.vdnh.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.vdnh.model.Message
import ru.vdnh.service.VdnhService

@RestController
class VdnhController(val service: VdnhService) {
    @Operation(summary = "Summary for get", description = "Description for get")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation")
        ]
    )
    @GetMapping
    fun index(): List<Message> = service.findMessages()

    @Operation(summary = "Summary for post", description = "Description for post")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation")
        ]
    )
    @PostMapping
    fun post(@RequestBody message: Message) {
        service.post(message)
    }
}