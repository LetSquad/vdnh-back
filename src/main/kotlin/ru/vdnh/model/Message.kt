package ru.vdnh.model

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Schema(description = "Message model")
@Table("messages")
data class Message(@Id val id: String?, val text: String)