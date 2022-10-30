package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.PlaceMapper
import ru.vdnh.repository.PlaceRepository

@Service
class PlaceService(
    val placeRepository: PlaceRepository,
    val placeMapper: PlaceMapper
) {

    fun getActivePlacesBySubject(subjectCode: String) =
        placeRepository.getAllActiveWhereSubject(subjectCode).map { placeMapper.entityToDomain(it) }

    fun getByCoordinatesId(coordinatesId: Long) =
        placeRepository.getByCoordinatesId(coordinatesId ?: throw RuntimeException("FUCK"))
            .let { placeMapper.entityToDomain(it) }

}
