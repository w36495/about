package com.w36495.about.data.repository

import com.w36495.about.domain.entity.Think
import kotlinx.coroutines.flow.Flow

interface ThinkRepository {

    suspend fun getThinkById(thinkId: Long): Flow<Think>

    suspend fun getThinkListByTopicId(topicId: Long): Flow<List<Think>>

    suspend fun getCountOfThinkListByTopicId(topicId: Long): Flow<String>

    suspend fun insertThink(think: Think): String

    suspend fun updateThink(thinkId: Long, think: String, updateDate: String): String

    suspend fun deleteThinkById(thinkId: Long): String

}