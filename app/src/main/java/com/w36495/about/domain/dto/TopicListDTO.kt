package com.w36495.about.domain.dto

data class TopicListDTO(
    val id: Long,
    val topic: String,
    val countOfThink: Int = 0,
    val registDate: String,
    val updateDate: String
)