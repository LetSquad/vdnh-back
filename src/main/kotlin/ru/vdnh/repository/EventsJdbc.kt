package ru.vdnh.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.vdnh.model.entity.EventEntity
import ru.vdnh.repository.mapper.EventRowMapper
import java.math.BigInteger

@Repository
class EventsJdbc(
    private val jdbcTemplate: JdbcTemplate,
    private val eventRowMapper: EventRowMapper
) : EventRepository {

    override fun findEvent(id: BigInteger): EventEntity {
        return jdbcTemplate.queryForObject(
            "SELECT e.id, e.title, e.title_en, e.title_cn, " +
                    "e.priority, e.url, e.image_url, e.is_active, e.coordinates_id," +
                    "e.schedule_id, e.type_code, e.subject_code, e.created_at, e.visit_time_minutes, e.type_code," +
                    "e.subject_code, e.payment_conditions, e.placement, e.start_date, e.finish_date, c.latitude, c.longitude, " +
                    "c.connections, c.load_factor, s.monday, s.tuesday, s.wednesday, s.thursday, s.friday, " +
                    "s.saturday, s.sunday, s.additional_info, lt.name, lt.name_en, lt.name_cn, lt.icon_code, lt.icon_color " +
                    "FROM event e " +
                    "LEFT JOIN coordinates c on e.coordinates_id = c.id " +
                    "LEFT JOIN schedule s on e.schedule_id = s.id " +
                    "LEFT JOIN location_type lt on e.type_code = lt.code " +
                    "where e.id = ?", eventRowMapper, id
        )!!
    }

    override fun getAllEvents(): List<EventEntity> {
        return jdbcTemplate.query(
            "SELECT e.id, e.title, e.title_en, e.title_cn, " +
                    "e.priority, e.url, e.image_url, e.is_active, e.coordinates_id," +
                    "e.schedule_id, e.type_code, e.subject_code, e.created_at, e.visit_time_minutes, e.type_code," +
                    "e.subject_code, e.payment_conditions, e.placement, e.start_date, e.finish_date, c.latitude, c.longitude, " +
                    "c.connections, c.load_factor, s.monday, s.tuesday, s.wednesday, s.thursday, s.friday, " +
                    "s.saturday, s.sunday, s.additional_info, lt.name, lt.name_en, lt.name_cn, lt.icon_code, lt.icon_color FROM event e " +
                    "LEFT JOIN coordinates c on e.coordinates_id = c.id " +
                    "LEFT JOIN schedule s on e.schedule_id = s.id " +
                    "LEFT JOIN location_type lt on e.type_code = lt.code ", eventRowMapper
        )
    }

    override fun getPlacesByEventId(eventId: Long): List<Long> {
        return jdbcTemplate.query("SELECT place_id from event_place where event_id = ?", { rs, _ ->
            rs.getLong("place_id")
        }, eventId)
    }
}
