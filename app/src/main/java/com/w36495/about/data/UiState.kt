package com.w36495.about.data

import com.w36495.about.domain.dto.TopicListDTO
import com.w36495.about.domain.entity.Comment
import com.w36495.about.domain.entity.Think
import com.w36495.about.domain.entity.Topic

sealed class TopicUiState {
    object Loading : TopicUiState()
    data class Success(val list: List<Topic>) : TopicUiState()
    data class Failed(val message: String?) : TopicUiState()
}

sealed class TopicListUiState {
    object Loading : TopicListUiState()
    object Empty : TopicListUiState()
    data class Success(val topicList: List<TopicListDTO>) : TopicListUiState()
    data class Failed(val tag: String, val message: String) : TopicListUiState()
}

sealed class ThinkUiState {
    object Loading : ThinkUiState()
    object Empty : ThinkUiState()
    data class Success(val thinkList: List<Think>) : ThinkUiState()
    data class Failed(val message: String?) : ThinkUiState()
}

sealed class CommentUiState {
    object Loading : CommentUiState()
    object Empty : CommentUiState()
    data class Success(val thinkList: List<Comment>) : CommentUiState()
    data class Failed(val message: String?) : CommentUiState()
}