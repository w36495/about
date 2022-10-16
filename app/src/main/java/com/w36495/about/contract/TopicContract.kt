package com.w36495.about.contract

import com.w36495.about.domain.entity.Topic

interface TopicContract {

    interface View {
        fun showTopicList(topicList: List<Topic>)
        fun showTopic(topic: Topic)
        fun showError(tag: String, message: String?)
        fun showToast(message: String?)
    }

    interface Presenter {
        fun saveTopic(topic: Topic)
        fun getTopic(id: Long)
        fun getTopicList()
        fun deleteTopicById(id: Long)
        fun deleteThinkListByTopicId(id: Long)
    }

}
