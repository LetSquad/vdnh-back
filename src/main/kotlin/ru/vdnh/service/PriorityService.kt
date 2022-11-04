package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.config.RouteNavigateConfigProperties
import ru.vdnh.model.domain.Location
import ru.vdnh.model.enums.CategoryType
import ru.vdnh.model.enums.LocationPlacement
import ru.vdnh.model.enums.PaymentConditions
import ru.vdnh.model.enums.PaymentRequirements
import ru.vdnh.model.enums.PopularNavigationType
import ru.vdnh.model.enums.RouteDifficultType
import java.time.LocalDateTime


@Service
class PriorityService(
    private val navigateProperties: RouteNavigateConfigProperties
) {

    fun getPriorityByLocationType(
        location: Location,
        param: CategoryType?,
    ): Int {
        return if (param == CategoryType.EVENT) navigateProperties.priority.eventStart else 0
    }

    fun getPriorityByPopular(
        location: Location,
        param: PopularNavigationType?,
    ): Int {
        if (location.priority == null) {
            return 0
        }

        if (param == null) {
            return location.priority
        }

        if (location.priority < 100 && param == PopularNavigationType.HIDDEN) {
            return navigateProperties.priority.coefficient
        }
        if (location.priority < 500 && param == PopularNavigationType.BALANCED) {
            return navigateProperties.priority.coefficient
        }
        if (param == PopularNavigationType.POPULAR) {
            return navigateProperties.priority.coefficient
        }

        return location.priority
    }

    fun getPriorityByRouteSpeed(
        location: Location,
        param: RouteDifficultType?
    ): Int {
        if (location.visitTime == null) {
            return 0
        }

        if (param == null) {
            return 0
        }

        val minutesVisitTime = location.visitTime.toMinutes()
        if (minutesVisitTime < 15 && param == RouteDifficultType.HARD) {
            return navigateProperties.priority.coefficient
        }
        if (minutesVisitTime < 30 && param == RouteDifficultType.MEDIUM) {
            return navigateProperties.priority.coefficient
        }
        if (param == RouteDifficultType.EASY) {
            return navigateProperties.priority.coefficient
        }

        return 0
    }

    fun getPriorityByLocationPlacement(
        location: Location,
        param: LocationPlacement?
    ): Int {
        if (location.placement == null) {
            return 0
        }

        if (param == null) {
            return 0
        }

        if (location.placement == param) {
            return navigateProperties.priority.coefficient
        }

        return 0
    }

    fun getPriorityByPaymentRequirements(
        location: Location,
        param: PaymentRequirements?
    ): Int {
        if (location.paymentConditions == null || param == null) return 0

        if (location.paymentConditions == PaymentConditions.FREE) {
            return when (param) {
                PaymentRequirements.FREE -> navigateProperties.priority.coefficient
                PaymentRequirements.CHEAP -> navigateProperties.priority.coefficient / 2
                else -> 0
            }
        }

        return 0
    }

    fun getPriorityByLoadFactor(
        location: Location,
        dateTime: LocalDateTime
    ): Int {
        if (location.coordinates == null) {
            return 0
        }

        return location.coordinates.loadFactor[dateTime.dayOfWeek]?.get(dateTime.toLocalTime())?.toInt()
            ?: return 0
    }

}
