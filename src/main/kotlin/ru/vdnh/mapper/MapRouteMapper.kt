package ru.vdnh.mapper

import com.mapbox.geojson.LineString
import org.springframework.stereotype.Component
import ru.vdnh.model.domain.Location
import ru.vdnh.model.dto.GeometryRouteDTO
import ru.vdnh.model.dto.MapPointDTO
import ru.vdnh.model.dto.MapRouteDataDTO
import ru.vdnh.model.dto.RouteDTO

@Component
class MapRouteMapper {

    fun toMapRouteDTO(locations: List<Location>, lineString: LineString) = MapRouteDataDTO(
        mapData = RouteDTO(
            geometry = GeometryRouteDTO(
                type = "LineString",
                coordinates = lineString.coordinates()
                    .map { listOf(it.longitude().toBigDecimal(), it.latitude().toBigDecimal()) }
            ),
            mapPoints = locations.map { MapPointDTO(it.locationId, it.locationCodeType) }
        )
    )
}
