package ru.vdnh.repository

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import ru.vdnh.model.entity.EventEntity
import java.math.BigInteger

interface EventRepository : CrudRepository<EventEntity, BigInteger> {
    @Query("select * from event where id = :id")
    fun findEvent(id: BigInteger): EventEntity
}