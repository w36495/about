package com.w36495.about.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class Topic(
    val topic: String,
    val color: String,
    val registDate: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var count: Int = 0
}