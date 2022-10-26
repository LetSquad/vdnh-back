package ru.vdnh.model.entity

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.math.BigInteger

@Schema(description = "Route model")
@Table("coordinates")
data class RouteEntity(@Id val id: BigInteger, val latitude: BigDecimal, val longitude: BigDecimal)