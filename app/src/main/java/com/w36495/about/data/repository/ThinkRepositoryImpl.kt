package com.w36495.about.data.repository

import com.w36495.about.data.local.ThinkDao
import com.w36495.about.domain.entity.Think
import kotlinx.coroutines.flow.Flow

class ThinkRepositoryImpl(private val thinkDao: ThinkDao) : ThinkRepository {
    override suspend fun getThinkById(thinkId: Long): Flow<Think> {
        return thinkDao.getThinkById(thinkId)
    }

    override suspend fun getThinkListByTopicId(topicId: Long): Flow<List<Think>> {
        return thinkDao.getThinkListByTopicId(topicId)
    }

    override suspend fun getCountOfThinkListByTopicId(topicId: Long): Flow<String> {
        return thinkDao.getCountOfThinkListByTopicId(topicId)
    }

    override suspend fun insertThink(think: Think): String {
        return thinkDao.insertThink(think).toString()
    }

    override suspend fun updateThink(thinkId: Long, think: String, updateDate: String): String {
        return thinkDao.updateThink(thinkId, think, updateDate).toString()
    }

    override suspend fun deleteThinkById(thinkId: Long): String {
        return thinkDao.deleteThinkById(thinkId).toString()
    }
}