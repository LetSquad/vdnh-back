package ru.vdnh.model.dto

data class ExceptionDTO(
    val code: String,
    val message: Throwable,
    val input: String?,
    val inputName: String?
)
