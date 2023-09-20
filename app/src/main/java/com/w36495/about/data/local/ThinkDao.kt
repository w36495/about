package com.w36495.about.data.local

import androidx.room.*
import com.w36495.about.domain.entity.Think
import kotlinx.coroutines.flow.Flow

@Dao
interface ThinkDao {
    @Query("SELECT * FROM thinks WHERE topicId = :topicId")
    fun getThinkListByTopicId(topicId: Long): Flow<List<Think>>

    @Query("SELECT * FROM thinks WHERE id = :thinkId")
    fun getThinkById(thinkId: Long): Flow<Think>

    @Query("SELECT count(*) FROM thinks WHERE topicId = :topicId")
    fun getCountOfThinkListByTopicId(topicId: Long): Flow<String>

    @Update
    suspend fun updateThink(think: Think)

    @Insert
    suspend fun insertThink(think: Think)

    @Query("DELETE FROM thinks WHERE topicId = :topicId")
    suspend fun deleteThinkByTopicId(topicId: Long)

    @Query("DELETE FROM thinks WHERE id = :thinkId")
    suspend fun deleteThinkById(thinkId: Long)

    @Query("DELETE FROM thinks")
    suspend fun deleteAllThinks()

    @Query("SELECT count(*) FROM thinks")
    suspend fun getThinkListCount(): Int
}