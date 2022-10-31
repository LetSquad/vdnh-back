package ru.vdnh.mapper

import org.springframework.stereotype.Component
import ru.vdnh.model.VdnhConstants.MAX_PRIORITY
import ru.vdnh.model.VdnhConstants.NAME
import ru.vdnh.model.VdnhConstants.NAME_CN
import ru.vdnh.model.VdnhConstants.NAME_EN
import ru.vdnh.model.VdnhConstants.PRIORITY_COEF
import ru.vdnh.model.VdnhConstants.SHORT_TITLE
import ru.vdnh.model.VdnhConstants.SHORT_TITLE_CN
import ru.vdnh.model.VdnhConstants.SHORT_TITLE_EN
import ru.vdnh.model.VdnhConstants.SHORT_TITLE_LENGTH
import ru.vdnh.model.VdnhConstants.TITLE
import ru.vdnh.model.VdnhConstants.TITLE_CN
import ru.vdnh.model.VdnhConstants.TITLE_EN
import ru.vdnh.model.VdnhConstants.VDNH_PREFIX
import ru.vdnh.model.VdnhConstants.ZOOM_COEF
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
        title = mapOf(Pair(TITLE, place.title), Pair(TITLE_EN, place.titleEn), Pair(TITLE_CN, place.titleCn)),
        shortTitle = mapOf(
            Pair(SHORT_TITLE, createShortTitle(place.title)),
            Pair(SHORT_TITLE_EN, createShortTitle(place.titleEn)),
            Pair(SHORT_TITLE_CN, createShortTitle(place.titleCn))
        ),
        type = mapOf(
            Pair(NAME, place.type.name),
            Pair(NAME_EN, place.type.nameEn),
            Pair(NAME_CN, place.type.nameCn)
        ),
        icon = place.type.iconCode,
        color = place.type.iconColor,
        url = VDNH_PREFIX + place.url,
        pic = VDNH_PREFIX + place.imageUrl,
        events = place.events,
        places = null
    )

    fun domainToDto(event: Event): PropertyDTO = PropertyDTO(
        category = CategoryType.EVENT,
        isVisible = true,
        zoom = calculateZoom(event.priority),
        title = mapOf(Pair(TITLE, event.title), Pair(TITLE_EN, event.titleEn), Pair(TITLE_CN, event.titleCn)),
        shortTitle = mapOf(
            Pair(SHORT_TITLE, createShortTitle(event.title)),
            Pair(SHORT_TITLE_EN, createShortTitle(event.titleEn)),
            Pair(SHORT_TITLE_CN, createShortTitle(event.titleCn))
        ),
        type = mapOf(
            Pair(NAME, event.type.name),
            Pair(NAME_EN, event.type.nameEn),
            Pair(NAME_CN, event.type.nameCn)
        ),
        icon = event.type.iconCode,
        color = event.type.iconColor,
        url = VDNH_PREFIX + event.url,
        pic = VDNH_PREFIX + event.imageUrl,
        places = event.places,
        events = null
    )

    private fun calculateZoom(priority: Int): Double {
        val normalizedPriority = if (priority > MAX_PRIORITY) MAX_PRIORITY else priority
        return normalizedPriority.toDouble() / MAX_PRIORITY * PRIORITY_COEF + ZOOM_COEF
    }

    private fun createShortTitle(title: String?): String? {
        if (title != null) {
            if (title.length <= SHORT_TITLE_LENGTH) return title
            return title.take(SHORT_TITLE_LENGTH).plus("...")
        }
        return null
    }
}
