package com.w36495.about.domain.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "comments",
    foreignKeys = [
        ForeignKey(
            entity = Think::class,
            parentColumns = ["id"],
            childColumns = ["thinkId"],
            onUpdate = CASCADE,
            onDelete = CASCADE
        )
    ]
)
data class Comment(
    @PrimaryKey
    val id: Long = System.currentTimeMillis(),
    val thinkId: Long,
    val comment: String,
    val registDate: String,
)