package com.w36495.about.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.w36495.about.domain.dto.TopicListDTO
import com.w36495.about.domain.entity.Topic
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao{

    @Query("SELECT * FROM topics WHERE id = :topicId")
    fun getTopicById(topicId: Long): Flow<Topic>

    @Query("""
        SELECT topics.id, topics.topic, COUNT(topicId) AS countOfThink, topics.registDate, topics.updateDate
        FROM topics
        LEFT JOIN thinks ON topics.id = thinks.topicId
        GROUP BY topics.id
    """)
    fun getTopicList(): Flow<List<TopicListDTO>>

    @Insert
    suspend fun insertTopic(topic: Topic)

    @Query("UPDATE topics SET topic = :topic, updateDate = :updateDate WHERE id = :topicId")
    suspend fun updateTopic(topicId: Long, topic: String, updateDate: String)

    @Query("DELETE FROM topics WHERE id = :topicId")
    suspend fun deleteTopicById(topicId: Long)

    @Query("DELETE FROM topics")
    suspend fun deleteAllTopicList()
}