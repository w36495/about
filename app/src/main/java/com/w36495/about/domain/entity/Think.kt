package com.w36495.about.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "thinks")
data class Think(
    @PrimaryKey
    val id: Long = System.currentTimeMillis(),
    val topicId: Long,
    val think: String,
    val registDate: String,
    val updateDate: String
)