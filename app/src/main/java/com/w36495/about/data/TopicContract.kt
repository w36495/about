package com.w36495.about.data

interface TopicContract {

    interface View {
        fun showTopicList(topicList: List<Topic>)
        fun setTopic(topic: Topic)
    }

    interface Presenter {
        fun saveTopic(topic: Topic)
        fun getTopic(id: Long)
        fun getTopicList()
        fun deleteTopicById(id: Long)
        fun checkLengthOfTopic(lengthOfTopic: Int): Boolean
    }

}
