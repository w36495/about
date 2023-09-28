package com.w36495.about.ui.listener

import com.w36495.about.domain.entity.Think

interface ThinkListItemListener {

    fun onSwipeItem(thinkId: Long)

    fun onClickItem(position: Int, think: Think)

}