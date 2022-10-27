package ru.vdnh.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.math.BigInteger

@Table("coordinates")
data class RouteEntity(@Id val id: BigInteger, val latitude: BigDecimal, val longitude: BigDecimal)