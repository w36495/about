package com.w36495.about.listener

import com.w36495.about.data.Think
import com.w36495.about.data.Topic

interface DialogClickListener {

    fun onTopicSaveClicked(topic: Topic)

    fun onThinkSaveClicked(think: Think)

}