package com.w36495.about.data.repository

import com.w36495.about.domain.dto.TopicListDTO
import com.w36495.about.domain.entity.Topic
import kotlinx.coroutines.flow.Flow

interface TopicRepository {

    suspend fun getTopicById(topicId: Long): Flow<Topic>

    suspend fun getTopicList(): Flow<List<TopicListDTO>>

    suspend fun insertTopic(topic: Topic): String

    suspend fun updateTopic(topicId: Long, topic: String, updateDate: String): String

    suspend fun deleteTopicById(topicId: Long): String

    suspend fun deleteAllTopic(): String

}