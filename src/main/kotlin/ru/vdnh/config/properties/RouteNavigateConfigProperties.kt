package ru.vdnh.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration

@ConstructorBinding
@ConfigurationProperties(prefix = "app.navigation")
data class RouteNavigateConfigProperties(
    val routeVariantsCount: Int,
    val toFoodLocationDurationMinutes: Int,
    val locationMergeStrategy: Int,
    val timeToRoutePercent: Double,
    val priority: PriorityConfigProperties,
    val default: DefaultConfigProperties,
)

data class PriorityConfigProperties(
    val coefficient: Int,
    val start: Int,
    val eventStart: Int
)

data class DefaultConfigProperties(
    val routeDuration: Duration,
    val visitDuration: Duration,
    val startPlaceId: Long,
    val maxLocationCountInRoute: Int,
    val minLocationCountInRoute: Int,
)
