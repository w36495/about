package com.w36495.about.data.repository

import com.w36495.about.data.Think
import com.w36495.about.data.local.ThinkDao

class ThinkRepository(private val thinkDao: ThinkDao) {

    suspend fun getThink(id: Long): Think = thinkDao.getThinkById(id)

    suspend fun getThinkList(topicId: Long): List<Think> = thinkDao.getThinkListByTopicId(topicId)

    suspend fun saveThink(think: Think) = thinkDao.insertThink(think)

    suspend fun updateThink(think: Think) = thinkDao.updateThink(think)

    suspend fun deleteThinkById(id: Long) = thinkDao.deleteThinkById(id)

    suspend fun deleteThinkByTopicId(id: Long) = thinkDao.deleteThinkByTopicId(id)

}