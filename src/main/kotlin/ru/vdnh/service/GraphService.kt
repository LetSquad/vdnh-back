package ru.vdnh.service

import org.jgrapht.graph.DefaultUndirectedWeightedGraph
import org.jgrapht.graph.DefaultWeightedEdge
import org.springframework.stereotype.Service
import ru.vdnh.getLogger
import ru.vdnh.model.domain.Location
import ru.vdnh.model.domain.RouteNode
import java.util.stream.Collectors
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


@Service
class GraphService(
    private val coordinatesService: CoordinatesService,
) {

    fun makeRouteNodeFromLocation(location: Location): RouteNode = coordinatesService.getRouteNodeByCoordinateId(location.coordinates.id)

    fun makeRouteNodesFormLocations(
        locations: List<Location>,
    ): MutableList<RouteNode> {
        return locations.stream()
            .map { coordinatesService.getRouteNodeByCoordinateId(it.coordinates.id) }
            .collect(Collectors.toList())
    }

    fun createGraphFromRouteNodes(routeNodes: List<RouteNode>): DefaultUndirectedWeightedGraph<RouteNode, DefaultWeightedEdge> {
        val resultGraph =
            DefaultUndirectedWeightedGraph<RouteNode, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)

        log.info("Routes graph initialization started")

        for (routeNode in routeNodes) {
            resultGraph.addVertex(routeNode)
        }
        for (sourceNode in routeNodes) {
            for (connectedNode in routeNodes) {
                resultGraph.addEdge(sourceNode, connectedNode)
                    ?.also {
                        val distance = distanceInMeters(sourceNode, connectedNode)
                        if (distance > 0) {
                            resultGraph.setEdgeWeight(it, distance)
                        }
                    }
            }

        }

        log.info("Routes graph initialized with ${resultGraph.vertexSet().size} vertices and ${resultGraph.edgeSet().size} edges")

        return resultGraph
    }

    private fun distanceInMeters(node1: RouteNode, node2: RouteNode): Double {
        val theta = node1.longitude.toDouble() - node2.longitude.toDouble()
        var dist = sin(deg2rad(node1.latitude.toDouble())) * sin(deg2rad(node2.latitude.toDouble())) +
                cos(deg2rad(node1.latitude.toDouble())) * cos(deg2rad(node2.latitude.toDouble())) *
                cos(deg2rad(theta))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= MINUTES_IN_DEGREE * STATUE_MILES_IN_NAUTICAL_MILES
        dist *= KM_IN_MILES
        dist *= METERS_IN_KM
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    companion object {
        private val log = getLogger<GraphService>()

        private const val MINUTES_IN_DEGREE = 60
        private const val STATUE_MILES_IN_NAUTICAL_MILES = 1.1515
        private const val KM_IN_MILES = 1.609344
        private const val METERS_IN_KM = 1000
    }
}
