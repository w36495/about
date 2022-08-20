package com.w36495.about.data.repository

import com.w36495.about.data.Topic
import com.w36495.about.data.local.TopicDao

class TopicRepository(private val topicDao: TopicDao) {

    suspend fun getTopic(id: Long): Topic = topicDao.getTopicById(id)

    suspend fun getTopicList(): List<Topic> = topicDao.getTopics()

    suspend fun saveTopic(topic: Topic) = topicDao.insertTopic(topic)

    suspend fun deleteTopicById(topicId: Long) = topicDao.deleteTopicById(topicId)
}