package com.w36495.about.listener

import com.w36495.about.data.Think

interface ThinkListItemClickListener {

    fun onThinkListItemClicked(position: Int, think: Think)

}