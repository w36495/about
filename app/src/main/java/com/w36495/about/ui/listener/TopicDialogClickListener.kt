package com.w36495.about.ui.listener

import com.w36495.about.domain.entity.Topic

interface TopicDialogClickListener {

    fun onTopicSaveClicked(topic: Topic): Boolean

    fun onErrorTopicSaved()

}