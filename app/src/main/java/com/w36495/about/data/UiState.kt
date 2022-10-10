package com.w36495.about.data

import com.w36495.about.domain.entity.Think
import com.w36495.about.domain.entity.Topic

sealed class TopicUiState {
    object Loading : TopicUiState()
    object Empty : TopicUiState()
    data class Success(val list: List<Topic>) : TopicUiState()
    data class Failed(val message: String?) : TopicUiState()
}

sealed class ThinkUiState {
    object Loading : ThinkUiState()
    object Empty : ThinkUiState()
    data class Success(val thinkList: List<Think>) : ThinkUiState()
    data class Failed(val message: String?) : ThinkUiState()
}