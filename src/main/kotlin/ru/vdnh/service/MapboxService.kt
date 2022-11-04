package ru.vdnh.service

import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import org.springframework.stereotype.Service
import ru.vdnh.config.MapboxConfigProperties
import ru.vdnh.exception.MapboxException
import ru.vdnh.mapper.MapRouteMapper
import ru.vdnh.model.domain.Location
import ru.vdnh.model.dto.RouteDTO
import ru.vdnh.model.enums.MovementRouteType


@Service
class MapboxService(
    private val mapRouteMapper: MapRouteMapper,
    private val mapboxConfigProperties: MapboxConfigProperties
) {

    fun makeRoute(
        locations: List<Location>,
        movementType: MovementRouteType
    ): RouteDTO {
        val points: List<Point> = locations
            .map { Point.fromLngLat(it.coordinates.longitude.toDouble(), it.coordinates.latitude.toDouble()) }

        val response = MapboxDirections.builder()
            .profile(if (movementType == MovementRouteType.WALKING) DirectionsCriteria.PROFILE_WALKING else DirectionsCriteria.PROFILE_CYCLING)
            .accessToken(mapboxConfigProperties.accessToken)
            .waypoints(points)
            .build()
            .executeCall()

        if (response.isSuccessful) {
            val geometryStr: String? =
                response.body()?.routes()?.get(0)?.geometry()
            val lineString: LineString = LineString.fromPolyline(geometryStr!!, PRECISION_6)

            return mapRouteMapper.toRouteDTO(locations, lineString)
        }

        throw MapboxException("Response from mapbox is not ok: [$response]")
    }
}
