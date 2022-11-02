package ru.vdnh.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.vdnh.model.entity.PlaceEntity
import ru.vdnh.repository.mapper.PlaceRowMapper
import java.math.BigInteger

@Repository
class PlaceJdbc(
    private val jdbcTemplate: JdbcTemplate,
    private val placeRowMapper: PlaceRowMapper
) : PlaceRepository {

    override fun getAllPlaces(): List<PlaceEntity> {
        return jdbcTemplate.query(
            "SELECT p.id, p.title, p.title_en, p.title_cn, " +
                "p.priority, p.url, p.image_url, p.tickets_url, p.is_active, p.schedule, p.coordinates_id, " +
                "p.type_code, p.subject_code, p.created_at, p.visit_time_minutes, p.type_code," +
                "p.subject_code, p.payment_conditions, p.placement, c.latitude, c.longitude, " +
                "c.connections, c.load_factor, lt.name, lt.name_en, lt.name_cn, lt.icon_code, lt.icon_color " +
                "FROM place p " +
                "LEFT JOIN coordinates c on p.coordinates_id = c.id " +
                "LEFT JOIN location_type lt on p.type_code = lt.code " +
                "WHERE p.is_active = true",
            placeRowMapper
        )
    }

    override fun getPlaceById(id: BigInteger): PlaceEntity {
        return jdbcTemplate.queryForObject(
            "SELECT p.id, p.title, p.title_en, p.title_cn, " +
                "p.priority, p.url, p.image_url, p.tickets_url, p.is_active, p.schedule, p.coordinates_id, " +
                "p.type_code, p.subject_code, p.created_at, p.visit_time_minutes, p.type_code," +
                "p.subject_code, p.payment_conditions, p.placement, c.latitude, c.longitude, " +
                "c.connections, c.load_factor, lt.name, lt.name_en, lt.name_cn, lt.icon_code, lt.icon_color " +
                "FROM place p " +
                "LEFT JOIN coordinates c on p.coordinates_id = c.id " +
                "LEFT JOIN location_type lt on p.type_code = lt.code " +
                "WHERE p.id = ?",
            placeRowMapper, id
        )!!
    }

    override fun getEventsByPlaceId(placeId: Long): List<Long> {
        return jdbcTemplate.query("SELECT event_id from event_place where place_id = ?", { rs, _ ->
            rs.getLong("event_id")
        }, placeId)
    }
}
