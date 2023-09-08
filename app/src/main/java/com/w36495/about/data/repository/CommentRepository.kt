package com.w36495.about.data.repository

import com.w36495.about.domain.entity.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    suspend fun getAllCommentList(thinkId: Long): Flow<List<Comment>>

    suspend fun getCountCommentList(thinkId: Long): Flow<Int>

    suspend fun insertComment(comment: Comment): String

    suspend fun deleteCommentById(commentId: Long): String

    suspend fun deleteAllComment(thinkId: Long): String
}