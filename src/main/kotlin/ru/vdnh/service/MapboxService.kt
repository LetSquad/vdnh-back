package ru.vdnh.service

import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import org.springframework.stereotype.Service
import ru.vdnh.model.MapboxConfigProperties
import ru.vdnh.model.domain.Location
import ru.vdnh.model.dto.GeometryRouteDTO
import ru.vdnh.model.dto.MapPointDTO
import ru.vdnh.model.dto.MapRouteDataDTO
import ru.vdnh.model.dto.RouteDTO


@Service
class MapboxService(
    val mapboxConfigProperties: MapboxConfigProperties
) {

    fun makeRoute(locations: List<Location>): MapRouteDataDTO {
        val points: List<Point> = locations
            .map { Point.fromLngLat(it.coordinates.longitude.toDouble(), it.coordinates.latitude.toDouble()) }

        val builder: MapboxDirections.Builder = MapboxDirections.builder()
            .profile(DirectionsCriteria.PROFILE_WALKING)
            .accessToken(mapboxConfigProperties.token)
            .waypoints(points)

        val response = builder.build().executeCall()

        if (response.isSuccessful) {
            val geometryStr: String? =
                response.body()?.routes()?.get(0)?.geometry()
            val lineString: LineString = LineString.fromPolyline(geometryStr!!, PRECISION_6)

            return MapRouteDataDTO(
                RouteDTO(
                    GeometryRouteDTO(
                        "LineString",
                        lineString.coordinates().map { listOf(it.longitude().toBigDecimal(), it.latitude().toBigDecimal()) }
                        ),
                    locations.map { MapPointDTO(it.locationId, it.locationCodeType) }
                )
            )
        }

        throw RuntimeException("response not ok")
    }
}
