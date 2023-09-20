package com.w36495.about.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.w36495.about.domain.entity.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {

    @Query("SELECT * FROM comments WHERE thinkId = :thinkId")
    fun getAllCommentList(thinkId: Long): Flow<List<Comment>>

    @Query("SELECT count(*) FROM comments WHERE thinkId = :thinkId")
    fun getCountCommentList(thinkId: Long): Flow<Int>

    @Insert
    fun insertComment(comment: Comment)

    @Query("DELETE FROM comments WHERE id = :commentId")
    fun deleteCommentById(commentId: Long)

    @Query("DELETE FROM comments WHERE thinkId = :thinkId")
    fun deleteAllComment(thinkId: Long)

}