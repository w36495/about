package com.w36495.about.ui.listener

import com.w36495.about.domain.entity.Topic

interface TopicListClickListener {

    fun onTopicListItemClicked(topicId: Long)

    fun onTopicDeleteClicked(topicId: Long, topic: Topic)

}