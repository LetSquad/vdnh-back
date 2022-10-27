package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.RouteMapper
import ru.vdnh.model.dto.RouteDTOList
import ru.vdnh.repository.RouteRepository
import java.math.BigInteger

@Service
class RouteService(val routeRepository: RouteRepository, val routeMapper: RouteMapper) {
    fun findRoutes(idFrom: BigInteger, idTo: BigInteger): RouteDTOList {
        val routeFrom = routeRepository.findRoute(idFrom)
            .let { routeMapper.entityToDomain(it) }
            .let { routeMapper.domainToDTO(it) }

        val routeTo = routeRepository.findRoute(idTo)
            .let { routeMapper.entityToDomain(it) }
            .let { routeMapper.domainToDTO(it) }

        return RouteDTOList(listOf(routeFrom, routeTo))
    }
}