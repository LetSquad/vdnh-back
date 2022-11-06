package ru.vdnh.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.vdnh.exception.EntityNotFoundException
import ru.vdnh.getLogger
import ru.vdnh.model.dto.ErrorDTO

@RestControllerAdvice
class ErrorHandlingControllerAdvise {

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleEmptyResultDataAccessException(e: EntityNotFoundException): ErrorDTO {
        return ErrorDTO("entity.not.found", e.message)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(e: Exception): ErrorDTO {
        log.error("", e)
        return ErrorDTO("error", e.message)
    }

    companion object {
        private val log = getLogger<ErrorHandlingControllerAdvise>()
    }
}
