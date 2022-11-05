package ru.vdnh.controller

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.vdnh.model.dto.ExceptionDTO

@RestControllerAdvice
class ControllerAdvisor {
    @ExceptionHandler(value = [EmptyResultDataAccessException::class])
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleEmptyResultDataAccessException(ex: RuntimeException): ExceptionDTO {
        return ExceptionDTO("EmptyResultDataAccessException", ex, ex.message, null)
    }
}