package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.CoordinatesMapper
import ru.vdnh.model.dto.HeatmapDTO
import ru.vdnh.repository.CoordinatesRepository
import java.time.DayOfWeek
import java.time.LocalTime

@Service
class CoordinatesService(
    private val coordinatesMapper: CoordinatesMapper,
    private val coordinatesRepository: CoordinatesRepository
) {

    fun getHeatmap(day: DayOfWeek, time: String): HeatmapDTO {
        val parsedTime = LocalTime.parse(time)
        val hour: Int = if (parsedTime.minute < HALF_OF_HOUR || parsedTime.hour == LAST_HOUR) {
            parsedTime.hour
        } else {
            parsedTime.hour + 1
        }

        return coordinatesRepository.getAllCoordinates()
            .map { coordinatesMapper.entityToDomain(it) }
            .let { coordinatesMapper.domainListToHeatmapDTO(it, day, LocalTime.of(hour, 0)) }
    }

    companion object {
        private const val HALF_OF_HOUR = 30
        private const val LAST_HOUR = 23
    }
}