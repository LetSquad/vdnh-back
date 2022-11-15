package ru.vdnh.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "vdnh")
data class VdnhConfigProperties(
    val baseUrl: String,
    val keyTitleRu: String,
    val keyTitleEn: String,
    val keyTitleCn: String
)
