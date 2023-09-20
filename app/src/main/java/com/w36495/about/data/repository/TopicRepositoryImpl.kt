package com.w36495.about.data.repository

import com.w36495.about.data.local.TopicDao
import com.w36495.about.domain.dto.TopicListDTO
import com.w36495.about.domain.entity.Topic
import kotlinx.coroutines.flow.Flow

class TopicRepositoryImpl(private val topicDao: TopicDao) : TopicRepository {
    override suspend fun getTopicById(topicId: Long): Flow<Topic> {
        return topicDao.getTopicById(topicId)
    }

    override suspend fun getTopicList(): Flow<List<TopicListDTO>> {
        return topicDao.getTopicList()
    }

    override suspend fun insertTopic(topic: Topic): String {
        return topicDao.insertTopic(topic).toString()
    }

    override suspend fun updateTopic(topicId: Long, topic: String, updateDate: String): String {
        return topicDao.updateTopic(topicId, topic, updateDate).toString()
    }

    override suspend fun deleteTopicById(topicId: Long): String {
        return topicDao.deleteTopicById(topicId).toString()
    }

    override suspend fun deleteAllTopic(): String {
        return topicDao.deleteAllTopicList().toString()
    }
}