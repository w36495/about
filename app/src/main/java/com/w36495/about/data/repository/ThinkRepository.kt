package com.w36495.about.data.repository

import com.w36495.about.domain.entity.Think
import kotlinx.coroutines.flow.Flow

interface ThinkRepository {

    suspend fun getThink(id: Long): Flow<Think>

    suspend fun getThinkList(topicId: Long): Flow<List<Think>>

    suspend fun getThinkListSize(topicId: Long): Flow<Int>

    suspend fun getThinkListCount(): Flow<Int>

    suspend fun saveThink(think: Think): Flow<String>

    suspend fun updateThink(thinkId: Long, think: String, updateDate: String): String

    suspend fun deleteThinkById(thinkId: Long): String

}