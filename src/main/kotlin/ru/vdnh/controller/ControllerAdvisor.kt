package ru.vdnh.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.vdnh.exception.EntityNotFoundException
import ru.vdnh.model.dto.ErrorDTO

@RestControllerAdvice
class ControllerAdvisor {
    @ExceptionHandler(value = [EntityNotFoundException::class])
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleEmptyResultDataAccessException(ex: EntityNotFoundException): ErrorDTO {
        return ErrorDTO("entity.not.found", ex.message)
    }

    @ExceptionHandler(value = [Exception::class])
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleException(ex: Exception): ErrorDTO {
        return ErrorDTO("Exception", ex.message)
    }
}