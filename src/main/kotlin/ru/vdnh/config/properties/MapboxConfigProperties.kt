package ru.vdnh.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "mapbox")
data class MapboxConfigProperties(
    val accessToken: String
)
