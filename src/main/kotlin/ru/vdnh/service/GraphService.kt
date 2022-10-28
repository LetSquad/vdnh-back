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
            for (targetCoordinates in sourceNode.connectedCoordinatesId) {
                routesGraph.addEdge(sourceNode, routeNodes.getValue(targetCoordinates))
                    ?.also { routesGraph.setEdgeWeight(it, 1.0) } //TODO: set weights
            }
        }

        log.info("Routes graph initialized with ${routesGraph.vertexSet().size} vertices and ${routesGraph.edgeSet().size} edges")
    }

    companion object {
        private val log = getLogger<GraphService>()
    }
}
