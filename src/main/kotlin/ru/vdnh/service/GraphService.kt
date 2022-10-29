package ru.vdnh.service

import org.jgrapht.graph.DefaultUndirectedWeightedGraph
import org.jgrapht.graph.DefaultWeightedEdge
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Service
import ru.vdnh.getLogger
import ru.vdnh.mapper.RouteMapper
import ru.vdnh.model.domain.RouteNode
import ru.vdnh.repository.CoordinatesRepository


@Service
class GraphService(
    private val routeMapper: RouteMapper,
    private val coordinatesRepository: CoordinatesRepository
) : ApplicationRunner {

    private val routesGraph = DefaultUndirectedWeightedGraph<RouteNode, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)

    override fun run(args: ApplicationArguments) {
        log.info("Routes graph initialization started")

        val routeNodes: Map<Long, RouteNode> = coordinatesRepository.getAllCoordinates()
            .map { routeMapper.coordinatesEntityToNodeDomain(it) }
            .associateBy { it.coordinatesId }

        for (routeNode in routeNodes.values) {
            routesGraph.addVertex(routeNode)
        }
        for (sourceNode in routeNodes.values) {
            for (connectedNode in routeNodes.values) {
                routesGraph.addEdge(sourceNode, connectedNode)
                    ?.also {
                        val distance = distanceInMeters(sourceNode, connectedNode)
                        if (distance > 0) {
                            routesGraph.setEdgeWeight(it, distance)
                        }
                    }
            }

        }

        log.info("Routes graph initialized with ${routesGraph.vertexSet().size} vertices and ${routesGraph.edgeSet().size} edges")
    }

    private fun distanceInMeters(node1: RouteNode, node2: RouteNode): Double {
        val theta = node1.longitude.toDouble() - node2.longitude.toDouble()
        var dist = Math.sin(deg2rad(node1.latitude.toDouble())) * Math.sin(deg2rad(node2.latitude.toDouble())) +
                Math.cos(deg2rad(node1.latitude.toDouble())) * Math.cos(deg2rad(node2.latitude.toDouble())) *
                Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        dist = dist * 1.609344
        dist = dist * 1000
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
    }
}
