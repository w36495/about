package com.w36495.about.data.repository

import com.w36495.about.domain.entity.Think
import kotlinx.coroutines.flow.Flow

interface ThinkRepository {

    suspend fun getThink(id: Long): Flow<Think>

    suspend fun getThinkList(topicId: Long): Flow<List<Think>>

    suspend fun getThinkListSize(topicId: Long): Flow<Int>

    suspend fun getThinkListCount(): Flow<Int>

    suspend fun saveThink(think: Think): Flow<String>

    suspend fun updateThink(think: Think): Flow<String>

    suspend fun deleteThinkById(id: Long): Flow<String>

    suspend fun deleteThinkByTopicId(id: Long): Flow<String>

    suspend fun deleteAllThink(): Flow<String>

}