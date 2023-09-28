package com.w36495.about.data.repository

import com.w36495.about.data.local.ThinkDao
import com.w36495.about.domain.entity.Think
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ThinkRepositoryImpl(private val thinkDao: ThinkDao) : ThinkRepository {

    override suspend fun getThink(id: Long): Flow<Think> = flow {
        val think = thinkDao.getThinkById(id)
        emit(think)
    }

    override suspend fun getThinkList(topicId: Long): Flow<List<Think>> = flow {
        val thinks = thinkDao.getThinkListByTopicId(topicId)
        emit(thinks)
    }

    override suspend fun getThinkListSize(topicId: Long): Flow<Int> = flow {
        val size = thinkDao.getThinkListSizeByTopicId(topicId)
        emit(size)
    }

    override suspend fun getThinkListCount(): Flow<Int> = flow {
        val count = thinkDao.getThinkListCount()
        emit(count)
    }

    override suspend fun saveThink(think: Think): Flow<String> = flow {
        val result = thinkDao.insertThink(think)
        emit(result.toString())
    }

    override suspend fun updateThink(think: Think): Flow<String> = flow {
        val result = thinkDao.updateThink(think)
        emit(result.toString())
    }

    override suspend fun deleteThinkById(id: Long): Flow<String> = flow {
        val result = thinkDao.deleteThinkById(id)
        emit(result.toString())
    }

    override suspend fun updateThink(thinkId: Long, think: String, updateDate: String): String {
        return thinkDao.updateThink(thinkId, think, updateDate).toString()
    }

    override suspend fun deleteThinkById(thinkId: Long): String {
        return thinkDao.deleteThinkById(thinkId).toString()
    }

}