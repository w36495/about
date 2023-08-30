package com.w36495.about.contract

import com.w36495.about.domain.entity.Comment

interface CommentContract {

    interface View {
        fun showToast(message: String)
        fun showErrorToast(message: String)
    }

    interface Presenter {
        fun getCommentList(thinkId: Long)
        fun saveComment(comment: Comment)
    }
}