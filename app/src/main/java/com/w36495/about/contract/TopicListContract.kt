package com.w36495.about.contract

import com.w36495.about.domain.entity.Topic

interface TopicListContract {
    interface View {
        fun showError(tag: String, message: String?)
        fun showToast(message: String?)
    }

    interface Presenter {
        fun saveTopic(topic: Topic)
        fun getTopicList()
        fun updateTopic(topicId: Long, topic: String, updateDate: String)
        fun deleteTopicById(topicId: Long)
    }
}
