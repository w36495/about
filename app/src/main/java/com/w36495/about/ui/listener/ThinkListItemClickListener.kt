package com.w36495.about.ui.listener

import com.w36495.about.domain.entity.Think

interface ThinkListItemClickListener {

    fun onThinkListItemClicked(position: Int, think: Think)

}