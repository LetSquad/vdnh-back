package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.model.domain.Coordinates
import ru.vdnh.model.domain.Place
import java.util.stream.Collectors


@Service
class NavigationService(
    val placeService: PlaceService,
    val priorityService: PriorityService
) {

    fun getCoordinatesBySubject(
        subjectCode: String,
        number: Int,
        loadingFactorCheck: Boolean
    ): List<Coordinates> {
        val placeList = placeService.getPlacesBySubject(subjectCode)

        val priorityList: List<Pair<Place, Int>> = placeList.stream()
            .filter { it.isActive }
            .map { Pair<Place, Int>(it, priorityService.getPriority(it, loadingFactorCheck)) }
            .collect(Collectors.toList())

        priorityList.sortedBy { it.second }

        return sortByDistance(placeList.take(number))
    }

    fun sortByDistance(places: List<Place>): List<Coordinates> {
        // TODO

        return listOf()
    }
}
