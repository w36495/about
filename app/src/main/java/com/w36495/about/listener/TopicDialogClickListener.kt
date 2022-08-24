package com.w36495.about.listener

import com.w36495.about.data.Topic

interface TopicDialogClickListener {

    fun onTopicSaveClicked(topic: Topic): Boolean

    fun onErrorTopicSaved()

}