package ru.vdnh.model.dto


data class PreparedRoutePreviewDataDTO(
    val id: Long,
    val title: Map<String, String?>,
    val previewImageUrl: String,
    val duration: Int
)
