package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.model.domain.Location
import ru.vdnh.repository.CoordinatesRepository


@Service
class PriorityService(
    val coordinatesRepository: CoordinatesRepository,
) {

    fun getPriority(location: Location, loadingFactorCheck: Boolean): Int {
        var priority = location.priority ?: 0

        if (loadingFactorCheck) {
            val loadFactor = coordinatesRepository.getLoadFactorByCoordinateId(location.coordinatesId)
            priority += loadFactor.getFactor()
        }

        return priority
    }

}
