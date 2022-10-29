package ru.vdnh.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import ru.vdnh.model.domain.Coordinates
import ru.vdnh.model.domain.LocationCoordinates
import ru.vdnh.model.dto.CoordinateDTO
import ru.vdnh.model.dto.HeatmapCoordinatesDTO
import ru.vdnh.model.dto.HeatmapDTO
import ru.vdnh.model.entity.CoordinatesEntity
import java.time.DayOfWeek
import java.time.LocalTime

@Component
class CoordinatesMapper(private val mapper: ObjectMapper) {

    fun domainListToHeatmapDTO(coordinates: List<Coordinates>, day: DayOfWeek, time: LocalTime) = HeatmapDTO(
        day = day,
        time = time,
        heatmap = coordinates.mapNotNull { domainToHeatmapDTO(it, day, time) }
    )

    fun domainToDTO(coordinate: Coordinates) = CoordinateDTO(
        id = coordinate.id,
        latitude = coordinate.latitude,
        longitude = coordinate.longitude
    )

    fun entityToDomain(coordinates: CoordinatesEntity) = Coordinates(
        id = coordinates.id,
        latitude = coordinates.latitude,
        longitude = coordinates.longitude,
        connections = coordinates.connections?.let { mapper.readValue(it) } ?: emptyList(),
        loadFactor = coordinates.loadFactor?.let { mapper.readValue<Map<String, Map<String, Double>>>(it) }
            ?.entries
            ?.associate { (day, workload) ->
                DayOfWeek.valueOf(day.uppercase()) to workload.mapKeys { (time, _) -> LocalTime.parse(time) }
            }
            ?: emptyMap()
    )

    fun entityToLocationDomain(coordinates: CoordinatesEntity) = LocationCoordinates(
        id = coordinates.id,
        latitude = coordinates.latitude,
        longitude = coordinates.longitude
    )

    private fun domainToHeatmapDTO(coordinates: Coordinates, day: DayOfWeek, time: LocalTime): HeatmapCoordinatesDTO? {
        val loadFactor: Double = coordinates.loadFactor[day]?.get(time)
            ?: return null

        return HeatmapCoordinatesDTO(
            latitude = coordinates.latitude,
            longitude = coordinates.longitude,
            loadFactor = loadFactor
        )
    }
}
