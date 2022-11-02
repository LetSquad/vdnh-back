package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.CoordinatesMapper
import ru.vdnh.mapper.RouteMapper
import ru.vdnh.model.domain.RouteNode
import ru.vdnh.model.dto.CoordinateDTOList
import ru.vdnh.model.dto.HeatmapDTO
import ru.vdnh.repository.CoordinatesRepository
import java.math.BigInteger
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Service
class CoordinatesService(
    private val coordinatesMapper: CoordinatesMapper,
    private val routeMapper: RouteMapper,
    private val coordinatesRepository: CoordinatesRepository
) {

    fun getHeatmap(day: DayOfWeek?, time: String?): HeatmapDTO {
        val parsedTime = time?.let { LocalTime.parse(time) } ?: LocalTime.now()
        val hour: Int = if (parsedTime.minute < HALF_OF_HOUR || parsedTime.hour == LAST_HOUR) {
            parsedTime.hour
        } else {
            parsedTime.hour + 1
        }

        val dayOfHeatmap: DayOfWeek = day ?: LocalDate.now().dayOfWeek
        return coordinatesRepository.getAllCoordinates()
            .map { coordinatesMapper.entityToDomain(it) }
            .let { coordinatesMapper.domainListToHeatmapDTO(it, dayOfHeatmap, LocalTime.of(hour, 0)) }
    }

    fun getRouteNodeByCoordinateId(id: BigInteger): RouteNode =
        coordinatesRepository.getCoordinatesById(id)
            .let { routeMapper.coordinatesEntityToNodeDomain(it) }

    fun findRoutes(idFrom: BigInteger, idTo: BigInteger): CoordinateDTOList {
        val coordinateFrom = coordinatesRepository.getCoordinatesById(idFrom)
            .let { coordinatesMapper.entityToDomain(it) }
            .let { coordinatesMapper.domainToDTO(it) }

        val coordinateTo = coordinatesRepository.getCoordinatesById(idTo)
            .let { coordinatesMapper.entityToDomain(it) }
            .let { coordinatesMapper.domainToDTO(it) }

        return CoordinateDTOList(listOf(coordinateFrom, coordinateTo))
    }

    companion object {
        private const val HALF_OF_HOUR = 30
        private const val LAST_HOUR = 23
    }
}
