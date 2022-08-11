package com.w36495.about.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.w36495.about.data.Topic

@Dao
interface TopicDao{
    @Query("SELECT * FROM topics")
    fun getTopics(): List<Topic>

    @Query("SELECT * FROM topics WHERE id = :topicId")
    fun getTopicById(topicId: Long): Topic

    @Query("SELECT topic FROM topics WHERE id = :topicId")
    fun getTopicTitleById(topicId: Long): String

    @Insert
    fun insertTopic(topic: Topic)

    @Query("DELETE FROM topics WHERE id = :topicId")
    fun deleteTopicById(topicId: Long)
}