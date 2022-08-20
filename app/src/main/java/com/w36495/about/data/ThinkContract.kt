package com.w36495.about.data

interface ThinkContract {

    interface View {
        fun showThinkList(thinkList: List<Think>)
        fun showThink(think: Think)
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