package com.w36495.about.ui.listener

import com.w36495.about.domain.dto.TopicListDTO

interface TopicListClickListener {

    fun onClickItem(topic: TopicListDTO)

    fun onLongClickItem(topic: TopicListDTO)

}