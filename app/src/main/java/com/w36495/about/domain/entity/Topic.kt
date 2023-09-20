package com.w36495.about.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class Topic(
    @PrimaryKey
    val id: Long = System.currentTimeMillis(),
    val topic: String,
    val registDate: String,
    val updateDate: String
)