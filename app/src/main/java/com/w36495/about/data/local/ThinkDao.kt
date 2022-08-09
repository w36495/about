package com.w36495.about.data.local

import androidx.room.*
import com.w36495.about.data.Think

@Dao
interface ThinkDao {
    @Query("SELECT * FROM thinks WHERE topicId = :topicId")
    fun getThinkListByTopicId(topicId: Long): List<Think>

    @Query("SELECT * FROM thinks WHERE id = :thinkId")
    fun getThinkById(thinkId: Long): Think

    @Query("SELECT count(*) FROM thinks WHERE topicId = :topicId")
    fun getThinkListCountByTopicId(topicId: Long): Int

    @Update
    fun updateThink(think: Think)

    @Insert
    fun insertThink(think: Think)

    @Query("DELETE FROM thinks WHERE topicId = :topicId")
    fun deleteThinkByTopicId(topicId: Long)

    @Query("DELETE FROM thinks WHERE id = :thinkId")
    fun deleteThinkById(thinkId: Long)
}