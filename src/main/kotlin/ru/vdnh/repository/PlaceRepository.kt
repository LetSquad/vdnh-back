package ru.vdnh.repository

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import ru.vdnh.model.entity.PlaceEntity
import java.math.BigInteger

interface PlaceRepository : CrudRepository<PlaceEntity, BigInteger> {
    @Query("select * from place where subject_code = :subjectCode")
    fun getAllWhereSubject(subjectCode: String): List<PlaceEntity>
}
