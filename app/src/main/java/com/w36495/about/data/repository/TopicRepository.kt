package com.w36495.about.data.repository

import com.w36495.about.domain.entity.Topic
import kotlinx.coroutines.flow.Flow

interface TopicRepository {

    suspend fun getTopic(id: Long): Flow<Topic>

    suspend fun getTopicList(): Flow<List<Topic>>

    suspend fun saveTopic(topic: Topic): Flow<String>

    suspend fun deleteTopicById(topicId: Long): Flow<String>

}