package com.w36495.about.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "thinks")
data class Think(
    var topicId: Long,
    val text: String,
    val registDate: String,
    val updateDate: String
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}