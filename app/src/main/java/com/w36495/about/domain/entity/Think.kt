package com.w36495.about.domain.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "thinks",
    foreignKeys = [
        ForeignKey(
            entity = Topic::class,
            parentColumns = ["id"],
            childColumns = ["topicId"],
            onDelete = CASCADE,
            onUpdate = CASCADE
        )
    ]
)
data class Think(
    @PrimaryKey
    val id: Long = System.currentTimeMillis(),
    val topicId: Long,
    val think: String,
    val registDate: String,
    val updateDate: String
) : Serializable