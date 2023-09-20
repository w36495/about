package com.w36495.about.domain.dto

import androidx.room.ColumnInfo

data class TopicListDTO(
    val id: Long,
    val topic: String,
    @ColumnInfo(name = "countOfThink")
    val countOfThink: Int,
    val registDate: String,
    val updateDate: String
)