package com.w36495.about.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.w36495.about.domain.dto.TopicListDTO

@Entity(tableName = "topics")
data class Topic(
    @PrimaryKey
    val id: Long = System.currentTimeMillis(),
    val topic: String,
    val registDate: String,
    val updateDate: String
) {
    fun toTopicListDTO(count: Int): TopicListDTO {
        return TopicListDTO(
            id = id,
            topic = topic,
            countOfThink = count,
            registDate = registDate,
            updateDate = updateDate
        )
    }
}