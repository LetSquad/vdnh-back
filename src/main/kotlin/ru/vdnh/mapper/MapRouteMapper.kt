package ru.vdnh.mapper

import com.mapbox.geojson.LineString
import org.springframework.stereotype.Component
import ru.vdnh.model.domain.Location
import ru.vdnh.model.dto.GeometryRouteDTO
import ru.vdnh.model.dto.MapPointDTO
import ru.vdnh.model.dto.MapPointTimeInfoDTO
import ru.vdnh.model.dto.RouteDTO
import java.util.*

@Component
class MapRouteMapper {

    fun toRouteDTO(
        locations: List<Location>,
        lineString: LineString,
        timeInfo: List<MapPointTimeInfoDTO>,
        distance: Double?,
        time: Double?
    ): RouteDTO {
        return RouteDTO(
            id = UUID.randomUUID(),
            geometry = GeometryRouteDTO(
                type = "LineString",
                coordinates = lineString.coordinates()
                    .map { listOf(it.longitude().toBigDecimal(), it.latitude().toBigDecimal()) }
            ),
            mapPoints = locations.map { MapPointDTO(it.locationId, it.locationCodeType, it.description) },
            mapPointTimes = timeInfo,
            distance = distance ?: 0.0,
            time = time ?: 0.0
        )
    }
}
