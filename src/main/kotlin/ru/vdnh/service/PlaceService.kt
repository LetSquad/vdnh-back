package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.mapper.PlaceMapper
import ru.vdnh.repository.PlaceRepository

@Service
class PlaceService(
    val placeRepository: PlaceRepository,
    val placeMapper: PlaceMapper
) {

    fun getPlacesBySubject(subjectCode: String) =
        placeRepository.getAllWhereSubject(subjectCode).map { placeMapper.entityToDomain(it) }

}
