package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.RouteMapper
import ru.vdnh.model.dto.RouteDTO
import ru.vdnh.repository.RouteRepository
import java.math.BigInteger

@Service
class RouteService(val routeRepository: RouteRepository, val routeMapper: RouteMapper) {
    fun findRoutes(idFrom: BigInteger, idTo: BigInteger): List<RouteDTO> {
        val routeFrom = routeMapper.domainToDTO(routeMapper.entityToDomain(routeRepository.findRoute(idFrom)))
        val routeTo = routeMapper.domainToDTO(routeMapper.entityToDomain(routeRepository.findRoute(idTo)))
        return listOf(routeFrom, routeTo)
    }
}