package com.w36495.about.data.repository

import com.w36495.about.domain.entity.Topic
import kotlinx.coroutines.flow.Flow

interface TopicRepository {

    suspend fun getTopicById(topicId: Long): Topic

    suspend fun getTopicList(): Flow<List<Topic>>

    suspend fun insertTopic(topic: Topic): String

    suspend fun updateTopic(topicId: Long, topic: String, updateDate: String): String

    suspend fun deleteTopicById(topicId: Long): String

    suspend fun deleteAllTopic(): String

}