package com.w36495.about.data

import com.w36495.about.data.repository.ThinkRepository
import com.w36495.about.data.repository.TopicRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TopicPresenter(
    private val topicRepository: TopicRepository,
    private val thinkRepository: ThinkRepository,
    private val topicContractView: TopicContract.View
) : TopicContract.Presenter {

    private lateinit var topicList: List<Topic>
    private lateinit var topic: Topic

    override fun saveTopic(topic: Topic) {
        CoroutineScope(Dispatchers.IO).launch {
            topicRepository.saveTopic(topic)
        }
    }

    override fun getTopic(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            topic = topicRepository.getTopic(id)
            topicContractView.setTopic(topic)
        }
    }

    override fun getTopicList() {
        CoroutineScope(Dispatchers.IO).launch {
            topicList = topicRepository.getTopicList()
            topicList.forEach { topic ->
                topic.count = thinkRepository.getThinkListSize(topic.id)
            }
            topicContractView.showTopicList(topicList)
        }
    }

    override fun deleteTopicById(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            topicRepository.deleteTopicById(id)
        }
    }

    override fun checkLengthOfTopic(lengthOfTopic: Int): Boolean {
        return lengthOfTopic in 1..30
    }
}