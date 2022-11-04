package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.CoordinatesMapper
import ru.vdnh.mapper.RouteMapper
import ru.vdnh.model.domain.RouteNode
import ru.vdnh.model.dto.HeatmapDTO
import ru.vdnh.repository.CoordinatesRepository
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Service
class CoordinatesService(
    private val coordinatesMapper: CoordinatesMapper,
    private val routeMapper: RouteMapper,
    private val coordinatesRepository: CoordinatesRepository
) {

    fun getHeatmap(dayNumber: Int?, time: String?): HeatmapDTO {
        val parsedTime = if (time == null) LocalTime.now() else LocalTime.parse(time)
        val hour: Int = if (parsedTime.minute < HALF_OF_HOUR || parsedTime.hour == LAST_HOUR) {
            parsedTime.hour
        } else {
            parsedTime.hour + 1
        }

        val dayOfHeatmap: DayOfWeek = if (dayNumber == null) LocalDate.now().dayOfWeek else DayOfWeek.of(dayNumber)
        return coordinatesRepository.getAllCoordinates()
            .map { coordinatesMapper.entityToDomain(it) }
            .let { coordinatesMapper.domainListToHeatmapDTO(it, dayOfHeatmap, LocalTime.of(hour, 0)) }
    }

    fun getRouteNodeByCoordinateId(id: Long): RouteNode =
        coordinatesRepository.getCoordinatesById(id)
            .let { routeMapper.coordinatesEntityToNodeDomain(it) }

    companion object {
        private const val HALF_OF_HOUR = 30
        private const val LAST_HOUR = 23
    }
}
