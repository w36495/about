package com.w36495.about.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.w36495.about.domain.entity.Topic
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao{
    @Query("SELECT * FROM topics")
    fun getTopics(): Flow<List<Topic>>

    @Query("SELECT * FROM topics WHERE id = :topicId")
    fun getTopicById(topicId: Long): Topic

    @Insert
    fun insertTopic(topic: Topic)

    @Query("DELETE FROM topics WHERE id = :topicId")
    fun deleteTopicById(topicId: Long): Int

    @Query("DELETE FROM topics")
    fun deleteAllTopic(): Int

    @Query("SELECT count(*) FROM topics")
    fun getTopicListCount(): Int
}