package com.w36495.about.data

import com.w36495.about.data.repository.ThinkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ThinkPresenter(
    private val thinkRepository: ThinkRepository,
    private val thinkContractView: ThinkContract.View
) : ThinkContract.Presenter {

    private lateinit var thinkList: List<Think>
    private lateinit var think: Think

    override fun getThink(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            think = thinkRepository.getThink(id)
            thinkContractView.showThink(think)
        }
    }

    override fun getThinkList(topicId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            thinkList = thinkRepository.getThinkList(topicId)
            thinkContractView.showThinkList(thinkList)
        }
    }

    override fun saveThink(think: Think) {
        CoroutineScope(Dispatchers.IO).launch {
            thinkRepository.saveThink(think)
        }
    }

    override fun updateThink(think: Think) {
        CoroutineScope(Dispatchers.IO).launch {
            thinkRepository.updateThink(think)
        }
    }

    override fun deleteThinkById(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            thinkRepository.deleteThinkById(id)
        }
    }

    override fun deleteThinkByTopicId(topicId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            thinkRepository.deleteThinkByTopicId(topicId)
        }
    }
}