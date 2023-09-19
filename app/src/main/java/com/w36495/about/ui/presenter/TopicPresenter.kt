package com.w36495.about.ui.presenter

import com.w36495.about.contract.TopicListContract
import com.w36495.about.domain.entity.Topic
import com.w36495.about.data.TopicListUiState
import com.w36495.about.data.repository.ThinkRepositoryImpl
import com.w36495.about.data.repository.TopicRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TopicPresenter(
    private val topicRepository: TopicRepositoryImpl,
    private val thinkRepository: ThinkRepositoryImpl,
    private val topicContractView: TopicListContract.View
) : TopicListContract.Presenter {

    private val _topicListUiState = MutableStateFlow<TopicListUiState>(TopicListUiState.Loading)
    val topicListUiState: StateFlow<TopicListUiState> = _topicListUiState.asStateFlow()

    companion object {
        const val TAG_TOPIC_LIST_PRESENTER: String = "TOPIC_LIST_PRESENTER"
    }

    override fun saveTopic(topic: Topic) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                topicRepository.insertTopic(topic)
                topicContractView.showToast("주제가 등록되었습니다.")
            } catch (exception: Exception) { }
        }
    }

    override fun getTopicList() {
        CoroutineScope(Dispatchers.IO).launch {
            topicRepository.getTopicList()
                .onStart {
                    _topicListUiState.value = TopicListUiState.Loading
                }
                .catch { exception ->
                    _topicListUiState.value = TopicListUiState.Failed(TAG_TOPIC_LIST_PRESENTER, exception.localizedMessage)
                }
                .collectLatest { topicList ->
                    topicList.forEach { topic ->
                        thinkRepository.getCountOfThinkListByTopicId(topic.id)
                            .collect { count ->
                                _topicListUiState.value = TopicListUiState.Success(topicList.map { it.toTopicListDTO(count.toInt()) })
                            }
                    }
                }
        }
    }

    override fun updateTopic(topicId: Long, topic: String, updateDate: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                topicRepository.updateTopic(topicId, topic, updateDate)
                topicContractView.showToast("수정이 완료되었습니다.")
            } catch (exception: Exception) { }
        }
    }

    override fun deleteTopicById(topicId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                topicRepository.deleteTopicById(topicId)
                topicContractView.showToast("삭제가 완료되었습니다.")
            } catch (exception: Exception) { }
        }
    }
}