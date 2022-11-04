package ru.vdnh.model.dto

import ru.vdnh.model.enums.CategoryType

data class MapPointDTO(
    val id: Long,
    val category: CategoryType
)
