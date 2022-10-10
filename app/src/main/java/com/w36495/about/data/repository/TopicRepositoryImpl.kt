package com.w36495.about.data.repository

import com.w36495.about.data.local.TopicDao
import com.w36495.about.domain.entity.Topic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TopicRepositoryImpl(private val topicDao: TopicDao) : TopicRepository {

    override suspend fun getTopic(id: Long): Flow<Topic> = flow {
        val topic = topicDao.getTopicById(id)
        emit(topic)
    }

    override suspend fun getTopicList(): Flow<List<Topic>> = flow {
        val topics = topicDao.getTopics()
        emit(topics)
    }

    override suspend fun saveTopic(topic: Topic): Flow<String> = flow {
        val topic = topicDao.insertTopic(topic)
        emit(topic.toString())
    }

    override suspend fun deleteTopicById(topicId: Long): Flow<String> = flow {
        val topic = topicDao.deleteTopicById(topicId)
        emit(topic.toString())
    }
}