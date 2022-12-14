package ru.vdnh.mapper

import org.springframework.stereotype.Component
import ru.vdnh.config.properties.VdnhConfigProperties
import ru.vdnh.model.domain.Event
import ru.vdnh.model.domain.Place
import ru.vdnh.model.domain.WorkingHours
import ru.vdnh.model.dto.LocationPropertiesDTO
import ru.vdnh.model.enums.CategoryType
import ru.vdnh.model.enums.LocationTag
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Component
class LocationPropertiesMapper(private val vdnhConfigProperties: VdnhConfigProperties) {

    fun domainToDto(place: Place): LocationPropertiesDTO {
        val workingHours: WorkingHours? = place.schedule?.findTodayWorkingHours()
        return LocationPropertiesDTO(
            category = CategoryType.PLACE,
            isVisible = true,
            zoom = calculateZoom(place.priority),
            title = mapOf(
                vdnhConfigProperties.keyTitleRu to place.title,
                vdnhConfigProperties.keyTitleEn to place.titleEn,
                vdnhConfigProperties.keyTitleCn to place.titleCn
            ),
            shortTitle = mapOf(
                KEY_SHORT_TITLE_RU to createShortTitle(place.title),
                KEY_SHORT_TITLE_EN to createShortTitle(place.titleEn),
                KEY_SHORT_TITLE_CN to createShortTitle(place.titleCn)
            ),
            type = mapOf(
                KEY_TYPE_RU to place.type.name,
                KEY_TYPE_EN to place.type.nameEn,
                KEY_TYPE_CN to place.type.nameCn
            ),
            tag = retrieveTag(place.subjectCode, place.typeCode),
            icon = place.type.iconCode,
            color = place.type.iconColor,
            url = vdnhConfigProperties.baseUrl + place.url,
            ticketsUrl = place.ticketsUrl,
            pic = place.imageUrl?.let { vdnhConfigProperties.baseUrl + it },
            scheduleClosingTime = workingHours?.to?.let { closingTime ->
                if (closingTime == LocalTime.MIDNIGHT.minusSeconds(1)) {
                    workingHoursFormatter.format(LocalTime.MIDNIGHT)
                } else {
                    workingHoursFormatter.format(closingTime)
                }
            },
            scheduleDayOff = workingHours?.isDayOff,
            scheduleAdditionalInfo = null,
            events = place.events,
            places = null,
            visitTime = place.visitTime.toSeconds()
        )
    }

    fun domainToDto(event: Event): LocationPropertiesDTO = LocationPropertiesDTO(
        category = CategoryType.EVENT,
        isVisible = true,
        zoom = calculateZoom(event.priority),
        title = mapOf(
            vdnhConfigProperties.keyTitleRu to event.title,
            vdnhConfigProperties.keyTitleEn to event.titleEn,
            vdnhConfigProperties.keyTitleCn to event.titleCn
        ),
        shortTitle = mapOf(
            KEY_SHORT_TITLE_RU to createShortTitle(event.title),
            KEY_SHORT_TITLE_EN to createShortTitle(event.titleEn),
            KEY_SHORT_TITLE_CN to createShortTitle(event.titleCn)
        ),
        type = mapOf(
            KEY_TYPE_RU to event.type.name,
            KEY_TYPE_EN to event.type.nameEn,
            KEY_TYPE_CN to event.type.nameCn
        ),
        tag = retrieveTag(event.subjectCode, event.typeCode),
        icon = event.type.iconCode,
        color = event.type.iconColor,
        url = vdnhConfigProperties.baseUrl + event.url,
        ticketsUrl = null,
        pic = event.imageUrl?.let { vdnhConfigProperties.baseUrl + it },
        scheduleClosingTime = null,
        scheduleDayOff = null,
        scheduleAdditionalInfo = null,
        places = event.places.map { it.id },
        events = null,
        visitTime = event.visitTime.toSeconds()
    )

    private fun retrieveTag(subjectCode: String?, typeCode: String?): LocationTag = when {
        subjectCode == "NATURE" || typeCode == "GREEN_ZONE" || typeCode == "PICNIC" || typeCode == "POND" -> LocationTag.NATURE
        subjectCode == "ART" -> LocationTag.ART
        subjectCode == "ANIMALS" -> LocationTag.ANIMALS
        subjectCode == "ARCHITECTURE" -> LocationTag.ARCHITECTURE
        subjectCode == "MOSCOW" -> LocationTag.MOSCOW
        subjectCode == "TECH" -> LocationTag.TECH
        subjectCode == "SPORT" || typeCode == "PLAYGROUND" -> LocationTag.SPORT
        subjectCode == "HISTORY" -> LocationTag.HISTORY
        subjectCode == "KIDS" -> LocationTag.KIDS
        typeCode == "FOOD" || typeCode == "VENDING" -> LocationTag.FOOD
        typeCode == "WC" -> LocationTag.WC
        typeCode == "BUS_STOP" || typeCode == "PARKING" || typeCode == "TAXI" || typeCode == "HIRE" -> LocationTag.TRANSPORT
        typeCode == "FIRST_AID" -> LocationTag.FIRST_AID
        typeCode == "ENTRANCE" || typeCode == "ENTRY" -> LocationTag.ENTRANCE
        else -> LocationTag.UNKNOWN
    }

    private fun calculateZoom(priority: Int): Double {
        val normalizedPriority = if (priority > MAX_PRIORITY) MAX_PRIORITY else priority
        return normalizedPriority.toDouble() / MAX_PRIORITY * PRIORITY_COEF + MINIMUM_ZOOM
    }

    private fun createShortTitle(title: String?): String? {
        if (title != null) {
            if (title.length <= KEY_SHORT_TITLE_LENGTH) return title
            return title.take(KEY_SHORT_TITLE_LENGTH).plus("...")
        }
        return null
    }

    companion object {
        private const val KEY_SHORT_TITLE_RU = "shortTitleRu"
        private const val KEY_SHORT_TITLE_EN = "shortTitleEn"
        private const val KEY_SHORT_TITLE_CN = "shortTitleCn"

        private const val KEY_TYPE_RU = "typeRu"
        private const val KEY_TYPE_EN = "typeEn"
        private const val KEY_TYPE_CN = "typeCn"

        private const val KEY_SHORT_TITLE_LENGTH = 30
        private const val MAX_PRIORITY = 200
        private const val PRIORITY_COEF = 6
        private const val MINIMUM_ZOOM = 14

        private val workingHoursFormatter = DateTimeFormatter.ofPattern("HH:mm")
    }
}
