package ru.vdnh.model.dto

data class NavigateDto(
    val startPlace: Long?,
    val finishPlace: Long?,
    val count: Int,
    val loadFactorCheck: Boolean,
    val subjects: List<String>
)
