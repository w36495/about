package com.w36495.about.data.local

import androidx.room.*
import com.w36495.about.domain.entity.Think

@Dao
interface ThinkDao {
    @Query("SELECT * FROM thinks WHERE topicId = :topicId")
    suspend fun getThinkListByTopicId(topicId: Long): List<Think>

    @Query("SELECT * FROM thinks WHERE id = :thinkId")
    suspend fun getThinkById(thinkId: Long): Think

    @Query("SELECT count(*) FROM thinks WHERE topicId = :topicId")
    suspend fun getThinkListSizeByTopicId(topicId: Long): Int

    @Update
    suspend fun updateThink(think: Think)

    @Insert
    suspend fun insertThink(think: Think)

    @Query("DELETE FROM thinks WHERE topicId = :topicId")
    suspend fun deleteThinkByTopicId(topicId: Long)

    @Query("DELETE FROM thinks WHERE id = :thinkId")
    suspend fun deleteThinkById(thinkId: Long)
}