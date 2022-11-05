package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.config.RouteNavigateConfigProperties
import ru.vdnh.model.domain.Location
import ru.vdnh.model.enums.CategoryType
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
        if (location.priority == null || param == null) return 0

        if (param == PopularNavigationType.POPULAR) {
            return when (location.priority) {
                in 0..100 -> navigateProperties.priority.coefficient
                in 101..500 -> navigateProperties.priority.coefficient / 2
                in 501..1000 -> -(navigateProperties.priority.coefficient)
                else -> -navigateProperties.priority.coefficient * 2
            }
        }
        if (param == PopularNavigationType.BALANCED) {
            return when (location.priority) {
                in 0..100 -> navigateProperties.priority.coefficient / 2
                in 101..500 -> navigateProperties.priority.coefficient
                in 501..1000 -> navigateProperties.priority.coefficient / 2
                else -> -navigateProperties.priority.coefficient * 2
            }
        }
        if (param == PopularNavigationType.HIDDEN) {
            return when (location.priority) {
                in 0..100 -> -(navigateProperties.priority.coefficient)
                in 101..500 -> navigateProperties.priority.coefficient / 2
                in 501..1000 -> navigateProperties.priority.coefficient
                else -> -navigateProperties.priority.coefficient * 2
            }
        }

        return 0
    }

    fun getPriorityByRouteDifficulty(
        location: Location,
        param: RouteDifficultType?
    ): Int {
        if (location.visitTime == null || param == null) return 0

        val visitTimeMinutes = location.visitTime.toMinutes()
        if (param == RouteDifficultType.HARD) {
            return when (visitTimeMinutes) {
                in 0..15 -> navigateProperties.priority.coefficient
                in 16..29 -> navigateProperties.priority.coefficient / 2
                in 30..60 -> -(navigateProperties.priority.coefficient)
                else -> -navigateProperties.priority.coefficient * 2
            }
        }
        if (param == RouteDifficultType.MEDIUM) {
            return when (visitTimeMinutes) {
                in 0..15 -> navigateProperties.priority.coefficient / 2
                in 16..29 -> navigateProperties.priority.coefficient
                in 30..60 -> navigateProperties.priority.coefficient / 2
                else -> -navigateProperties.priority.coefficient * 2
            }
        }
        if (param == RouteDifficultType.EASY) {
            return when (visitTimeMinutes) {
                in 0..29 -> -(navigateProperties.priority.coefficient)
                in 30..59 -> navigateProperties.priority.coefficient / 2
                in 60..120 -> navigateProperties.priority.coefficient
                else -> -navigateProperties.priority.coefficient * 2
            }
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

        return -navigateProperties.priority.coefficient
    }

    fun getPriorityByLoadFactor(
        location: Location,
        dateTime: LocalDateTime
    ): Int {
        val loadFactorProc =
            location.coordinates.loadFactor[dateTime.dayOfWeek]?.get(dateTime.toLocalTime())?.toInt() ?: 0
        return when (loadFactorProc * 100) {
            in 0..30 -> navigateProperties.priority.coefficient
            in 31..60 -> navigateProperties.priority.coefficient / 2
            in 61..100 -> -navigateProperties.priority.coefficient * 2 // понижаем приоритет у загруженного места
            else -> 0
        }
    }

}
