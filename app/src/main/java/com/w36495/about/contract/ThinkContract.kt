package com.w36495.about.contract

import com.w36495.about.domain.entity.Comment
import com.w36495.about.domain.entity.Think

interface ThinkContract {

    interface View {
        fun showToast(message: String)
        fun showErrorToast(message: String)
    }

    interface Presenter {
        fun getThink(thinkId: Long)
        fun updateThink(think: Think)
        fun deleteThink(thinkId: Long)
        fun getAllCommentList(thinkId: Long)
        fun insertComment(comment: Comment)
        fun deleteComment(commentId: Long)
        fun getCountCommentList(thinkId: Long)
    }
}