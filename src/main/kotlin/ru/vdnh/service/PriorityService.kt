package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.config.RouteNavigateConfigProperties
import ru.vdnh.model.domain.Location
import ru.vdnh.model.dto.VisitorCountDTO
import ru.vdnh.model.enums.CategoryType
import ru.vdnh.model.enums.LocationPlacement
import ru.vdnh.model.enums.PaymentConditions
import ru.vdnh.model.enums.PaymentRequirements
import ru.vdnh.model.enums.PlacementRouteType
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

    fun getPriorityByPlacementType(
        location: Location,
        param: PlacementRouteType?
    ): Int {
        if (location.placement == null || param == null || param == PlacementRouteType.IRRELEVANT) return 0

        if (param == PlacementRouteType.INDOORS) {
            return when (location.placement) {
                LocationPlacement.INDOORS -> navigateProperties.priority.coefficient
                LocationPlacement.OUTSIDE -> 0
            }
        }
        if (param == PlacementRouteType.OUTSIDE) {
            return when (location.placement) {
                LocationPlacement.INDOORS -> 0
                LocationPlacement.OUTSIDE -> navigateProperties.priority.coefficient
            }
        }

        return 0
    }

    fun getPriorityByVisitorTypeAndNumber(
        location: Location,
        param: VisitorCountDTO?
    ): Int {
        if (location.subjectCode == null || param == null) return 0

        var priority = 0

        if (param.kid != 0 && location.subjectCode == KIDS_TAG) {
            priority += navigateProperties.priority.coefficient / 2
        }

        if (isPairVisitor(param) && PAIR_PRIORITY_TYPE.contains(location.typeCode)) {
            priority += navigateProperties.priority.coefficient
        }

        if (isFamilyVisitor(param) && FAMILY_PRIORITY_TYPE.contains(location.typeCode)) {
            priority += navigateProperties.priority.coefficient
        }

        if (isKidGroupVisitor(param) && KIDS_GROUP_PRIORITY_TYPE.contains(location.typeCode)) {
            priority += navigateProperties.priority.coefficient
        }

        if (isAdultGroupVisitor(param) && ADULT_GROUP_PRIORITY_TYPE.contains(location.typeCode)) {
            priority += navigateProperties.priority.coefficient
        }

        return priority
    }

    fun isPairVisitor(param: VisitorCountDTO?): Boolean {
        if (param == null) {
            return false
        }

        return param.kid == 0 && param.adult == 2
    }

    fun isFamilyVisitor(param: VisitorCountDTO?): Boolean {
        if (param == null) {
            return false
        }

        return param.kid != null && param.kid > 0 && param.adult == 2
    }

    fun isKidGroupVisitor(param: VisitorCountDTO?): Boolean {
        if (param == null) {
            return false
        }

        return param.kid != null && param.kid > 2
    }

    fun isAdultGroupVisitor(param: VisitorCountDTO?): Boolean {
        if (param == null) {
            return false
        }

        return param.adult != null && param.adult > 3
    }

    companion object {
        private val KIDS_TAG = "KIDS"
        private val ADULT_GROUP_PRIORITY_TYPE = listOf("PICNIC", "ATTRACTION", "CONCERTS_AND_SHOWS", "ENTERTAINMENT", "QUEST", "SPORT")
        private val KIDS_GROUP_PRIORITY_TYPE = listOf("ATTRACTION", "CHILDRENS_PLAYGROUND", "QUEST", "FOR_CHILDREN", "ENTERTAINMENT", )
        private val FAMILY_PRIORITY_TYPE = listOf("PICNIC", "ATTRACTION", "CHILDRENS_PLAYGROUND", "QUEST", "EXCURSION", "MUSEUM", "COMBO")
        private val PAIR_PRIORITY_TYPE = listOf("PICNIC", "ATTRACTION", "QUEST", "EXHIBITION", "FILM_SCREENING", "LECTURES_AND_MASTER_CLASSES")
    }

}
