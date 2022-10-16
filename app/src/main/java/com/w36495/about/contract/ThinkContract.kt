package com.w36495.about.contract

import com.w36495.about.domain.entity.Think

interface ThinkContract {

    interface View {
        fun showThinkList(thinkList: List<Think>)
        fun showThink(think: Think)
        fun showToast(message: String)
        fun showError(message: String)
    }

    interface Presenter {
        fun getThink(id: Long)
        fun getThinkList(topicId: Long)
        fun saveThink(think: Think)
        fun updateThink(think: Think)
        fun deleteThinkById(id: Long)
        fun deleteThinkByTopicId(topicId: Long)
    }

}