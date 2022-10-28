package ru.vdnh.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.math.BigInteger

@Table("coordinates")
data class CoordinatesEntity(
    @Id val id: Long,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
    val connections: String?,
    val loadFactor: String?
)
