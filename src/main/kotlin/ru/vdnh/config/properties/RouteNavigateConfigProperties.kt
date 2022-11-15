package ru.vdnh.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "app.navigation")
data class RouteNavigateConfigProperties(
    val routeVariantsCount: Int,
    val toFoodLocationDurationMinutes: Int,
    val locationMergeStrategy: Int,
    val priority: PriorityConfigProperties,
    val default: DefaultConfigProperties,
)

data class PriorityConfigProperties(
    val coefficient: Int,
    val start: Int,
    val eventStart: Int
)

data class DefaultConfigProperties(
    val routeTimeMinutes: Int,
    val visitTimeMinutes: Int,
    val startPlaceId: Long,
    val maxLocationCountInRoute: Int,
    val minLocationCountInRoute: Int,
)
