package com.w36495.about.ui.listener

import com.w36495.about.domain.entity.Think

interface ThinkDialogClickListener {

    fun onThinkSaveClicked(think: Think)

    fun onThinkUpdateClicked(think: Think)

}