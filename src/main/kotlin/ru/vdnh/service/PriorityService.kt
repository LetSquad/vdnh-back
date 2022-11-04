package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.model.domain.Location
import ru.vdnh.model.enums.CategoryType
import ru.vdnh.model.enums.LocationPlacement
import ru.vdnh.model.enums.PaymentConditions
import ru.vdnh.model.enums.PopularNavigationType
import ru.vdnh.model.enums.RouteDifficultType
import java.time.LocalDateTime


@Service
class PriorityService {

    fun getPriorityByLocationType(
        location: Location,
        param: CategoryType?,
    ): Int {
        return if (param == CategoryType.EVENT) DEFAULT_EVENT_PRIORITY else 0
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
            return PRIORITY
        }
        if (location.priority < 500 && param == PopularNavigationType.BALANCED) {
            return PRIORITY
        }
        if (param == PopularNavigationType.POPULAR) {
            return PRIORITY
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
            return PRIORITY
        }
        if (minutesVisitTime < 30 && param == RouteDifficultType.MEDIUM) {
            return PRIORITY
        }
        if (param == RouteDifficultType.EASY) {
            return PRIORITY
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
            return PRIORITY
        }

        return 0
    }

    fun getPriorityByPaymentConditions(
        location: Location,
        param: PaymentConditions?
    ): Int {
        if (location.paymentConditions == null) {
            return 0
        }

        if (param == null) {
            return 0
        }

        if (location.paymentConditions == param) {
            return PRIORITY
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

    companion object {
        const val PRIORITY: Int = -50

        // TODO по базе у событий приоритет ниже чем у мест.
        //  Возможно, стоит ввести отдельный шаг приоритета под события
        const val DEFAULT_EVENT_PRIORITY: Int = -100
    }

}
