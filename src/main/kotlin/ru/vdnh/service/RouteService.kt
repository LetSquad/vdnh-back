package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.RouteMapper
import ru.vdnh.model.domain.RouteNode
import ru.vdnh.model.dto.RouteDTOList
import ru.vdnh.repository.RouteRepository
import java.math.BigInteger

@Service
class RouteService(
    private val placeService: PlaceService,
    private val routeRepository: RouteRepository,
    private val routeMapper: RouteMapper
) {

    val DEFAULT_START_NODE = getNode(BigInteger.valueOf(DEFAULT_ENTER_NODE_ID))

    fun getCoordinate(id: BigInteger) = routeRepository.findRoute(id).let { routeMapper.entityToDomain(it) }

    fun getNode(id: BigInteger) = routeRepository.findRoute(id).let { routeMapper.coordinatesEntityToNodeDomain(it) }

    fun getNodeByPlaceId(placeId: Long?): RouteNode? {
        if (placeId == null) {
            return null
        }

        val place = placeService.get(placeId)
        return routeRepository.findRoute(place.coordinates.id.toBigInteger()).let { routeMapper.coordinatesEntityToNodeDomain(it) }
    }

    fun getDto(id: BigInteger) = getCoordinate(id).let { routeMapper.domainToDTO(it) }

    fun findRoutes(idFrom: BigInteger, idTo: BigInteger) = RouteDTOList(listOf(getDto(idFrom), getDto(idTo)))

    companion object {
        // центральный павильон
        val DEFAULT_ENTER_NODE_ID = 80L
    }
}
