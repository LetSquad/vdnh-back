package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.model.domain.Place
import ru.vdnh.repository.CoordinatesRepository


@Service
class PriorityService(
    val coordinatesRepository: CoordinatesRepository,
) {

    fun getPriority(place: Place, loadingFactorCheck: Boolean): Int {
        var priority = place.priority ?: 0

        if (loadingFactorCheck) {
            val loadFactor = coordinatesRepository.getLoadFactorByCoordinateId(place.coordinatesId)
            priority += loadFactor.getFactor()
        }

        return priority
    }

}
