package com.w36495.about.data.repository

import com.w36495.about.data.local.CommentDao
import com.w36495.about.domain.entity.Comment
import kotlinx.coroutines.flow.Flow

class CommentRepositoryImpl(
    private val commentDao: CommentDao
) : CommentRepository {
    override suspend fun getAllCommentList(thinkId: Long): Flow<List<Comment>> {
        return commentDao.getAllCommentList(thinkId)
    }

    override suspend fun getCountCommentList(thinkId: Long): Flow<Int> {
        return commentDao.getCountCommentList(thinkId)
    }

    override suspend fun insertComment(comment: Comment): String {
        return commentDao.insertComment(comment).toString()
    }

    override suspend fun deleteCommentById(commentId: Long): String {
        return commentDao.deleteCommentById(commentId).toString()
    }

    override suspend fun deleteAllComment(thinkId: Long): String {
        return commentDao.deleteAllComment(thinkId).toString()
    }
}