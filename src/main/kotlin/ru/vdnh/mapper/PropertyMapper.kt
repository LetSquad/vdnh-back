package ru.vdnh.mapper

import org.springframework.stereotype.Component
import ru.vdnh.model.domain.Event
import ru.vdnh.model.domain.Place
import ru.vdnh.model.dto.PropertyDTO
import ru.vdnh.model.enums.CategoryType

@Component
class PropertyMapper {

    fun domainToDto(place: Place): PropertyDTO = PropertyDTO(
        category = CategoryType.PLACE,
        isVisible = true,
        zoom = calculateZoom(place.priority),
        title = mapOf(KEY_TITLE_RU to place.title, KEY_TITLE_EN to place.titleEn, KEY_TITLE_CN to place.titleCn),
        shortTitle = mapOf(
            KEY_SHORT_TITLE_RU to createShortTitle(place.title),
            KEY_SHORT_TITLE_EN to createShortTitle(place.titleEn),
            KEY_SHORT_TITLE_CN to createShortTitle(place.titleCn)
        ),
        type = mapOf(
            KEY_NAME_RU to place.type.name,
            KEY_NAME_EN to place.type.nameEn,
            KEY_NAME_CN to place.type.nameCn
        ),
        icon = place.type.iconCode,
        color = place.type.iconColor,
        url = VDNH_BASE_URL + place.url,
        pic = VDNH_BASE_URL + place.imageUrl,
        events = place.events,
        places = null
    )

    fun domainToDto(event: Event): PropertyDTO = PropertyDTO(
        category = CategoryType.EVENT,
        isVisible = true,
        zoom = calculateZoom(event.priority),
        title = mapOf(KEY_TITLE_RU to event.title, KEY_TITLE_EN to event.titleEn, KEY_TITLE_CN to event.titleCn),
        shortTitle = mapOf(
            KEY_SHORT_TITLE_RU to createShortTitle(event.title),
            KEY_SHORT_TITLE_EN to createShortTitle(event.titleEn),
            KEY_SHORT_TITLE_CN to createShortTitle(event.titleCn)
        ),
        type = mapOf(
            KEY_NAME_RU to event.type.name,
            KEY_NAME_EN to event.type.nameEn,
            KEY_NAME_CN to event.type.nameCn
        ),
        icon = event.type.iconCode,
        color = event.type.iconColor,
        url = VDNH_BASE_URL + event.url,
        pic = VDNH_BASE_URL + event.imageUrl,
        places = if (event.places != null) event.places.map { it.id } else listOf(),
        events = null
    )

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
        const val KEY_TITLE_RU = "titleRu"
        const val KEY_TITLE_EN = "titleEn"
        const val KEY_TITLE_CN = "titleCn"
        const val KEY_SHORT_TITLE_RU = "shortTitleRu"
        const val KEY_SHORT_TITLE_EN = "shortTitleEn"
        const val KEY_SHORT_TITLE_CN = "shortTitleCn"
        const val KEY_NAME_RU = "nameRu"
        const val KEY_NAME_EN = "nameEn"
        const val KEY_NAME_CN = "nameCn"
        const val KEY_SHORT_TITLE_LENGTH = 30
        const val MAX_PRIORITY = 200
        const val PRIORITY_COEF = 6
        const val MINIMUM_ZOOM = 14
        const val VDNH_BASE_URL = "vdnh.ru"
    }
}
