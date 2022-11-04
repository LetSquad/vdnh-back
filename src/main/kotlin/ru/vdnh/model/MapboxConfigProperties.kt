package ru.vdnh.model

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "mapbox")
data class MapboxConfigProperties(
    var token: String = ""
)
