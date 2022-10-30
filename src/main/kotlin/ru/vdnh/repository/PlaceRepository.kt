package ru.vdnh.repository

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import ru.vdnh.model.entity.PlaceEntity
import java.math.BigInteger

interface PlaceRepository : CrudRepository<PlaceEntity, BigInteger> {
    @Query("select * from place where is_active = true and subject_code = :subjectCode")
    fun getAllActiveWhereSubject(subjectCode: String): List<PlaceEntity>

    @Query("select * from place where coordinates_id = :coordinatesId")
    fun getByCoordinatesId(coordinatesId: Long): PlaceEntity

    @Query("select * from place where id = :id")
    fun get(id: Long): PlaceEntity
}
