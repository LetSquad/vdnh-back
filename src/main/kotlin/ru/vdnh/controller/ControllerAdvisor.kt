package ru.vdnh.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.vdnh.exception.EntityNotFoundException
import ru.vdnh.model.dto.ExceptionDTO

@RestControllerAdvice
class ControllerAdvisor {
    @ExceptionHandler(value = [EntityNotFoundException::class])
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleEmptyResultDataAccessException(ex: EntityNotFoundException): ExceptionDTO {
        return ExceptionDTO("entity.not.found", ex.message)
    }

    @ExceptionHandler(value = [Exception::class])
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleException(ex: Exception): ExceptionDTO {
        return ExceptionDTO("Exception", ex.message)
    }
}