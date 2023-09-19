package com.w36495.about.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.w36495.about.domain.entity.Topic
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao{
    @Query("SELECT * FROM topics")
    fun getTopicList(): Flow<List<Topic>>

    @Query("SELECT * FROM topics WHERE id = :topicId")
    suspend fun getTopicById(topicId: Long): Topic

    @Insert
    suspend fun insertTopic(topic: Topic)

    @Query("UPDATE topics SET topic = :topic, updateDate = :updateDate WHERE id = :topicId")
    suspend fun updateTopic(topicId: Long, topic: String, updateDate: String)

    @Query("DELETE FROM topics WHERE id = :topicId")
    suspend fun deleteTopicById(topicId: Long)

    @Query("DELETE FROM topics")
    suspend fun deleteAllTopicList()
}