package com.w36495.about.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey
    val id: Long = System.currentTimeMillis(),
    val thinkId: Long,
    val comment: String,
    val registDate: String,
)