package ru.vdnh.repository.mapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import ru.vdnh.model.entity.ScheduleEntity
import java.sql.ResultSet

@Component
class ScheduleRowMapper : RowMapper<ScheduleEntity> {

    override fun mapRow(rs: ResultSet, rowNum: Int): ScheduleEntity = ScheduleEntity(
        id = rs.getLong("schedule_id"),
        monday = rs.getString("monday"),
        tuesday = rs.getString("tuesday"),
        wednesday = rs.getString("wednesday"),
        thursday = rs.getString("thursday"),
        friday = rs.getString("friday"),
        saturday = rs.getString("saturday"),
        sunday = rs.getString("sunday"),
        additionalInfo = rs.getString("additional_info")
    )
}