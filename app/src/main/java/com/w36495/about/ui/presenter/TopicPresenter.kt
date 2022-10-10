package com.w36495.about.ui.presenter

import com.w36495.about.domain.entity.Topic
import com.w36495.about.contract.TopicContract
import com.w36495.about.data.TopicUiState
import com.w36495.about.data.repository.ThinkRepository
import com.w36495.about.data.repository.TopicRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TopicPresenter(
    private val topicRepository: TopicRepositoryImpl,
    private val thinkRepository: ThinkRepository,
    private val topicContractView: TopicContract.View
) : TopicContract.Presenter {

    private val TAG_INSERT: String = "TOPIC_INSERT"
    private val TAG_DELETE: String = "TOPIC_DELETE"

    private val _uiState = MutableStateFlow<TopicUiState>(TopicUiState.Loading)
    val uiState: StateFlow<TopicUiState> = _uiState.asStateFlow()

    override fun saveTopic(topic: Topic) {
        CoroutineScope(Dispatchers.IO).launch {
            topicRepository.saveTopic(topic)
                .catch { exception ->
                    topicContractView.showError(TAG_INSERT, exception.localizedMessage)
                }
                .collect {
                    topicContractView.showToast("새로운 주제가 저장되었습니다.")
                }
        }
    }

    override fun getTopic(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            topicRepository.getTopic(id)
                .onStart {
                    _uiState.value = TopicUiState.Loading
                }
                .catch { exception ->
                    TopicUiState.Failed(exception.localizedMessage)
                }
                .collect {
                    topicContractView.showTopic(it)
                }
        }
    }

    override fun getTopicList() {
        CoroutineScope(Dispatchers.IO).launch {
            topicRepository.getTopicList()
                .onStart {
                    _uiState.value = TopicUiState.Loading
                }
                .catch { exception ->
                    TopicUiState.Failed(exception.localizedMessage)
                }
                .collect {
                    _uiState.value = TopicUiState.Success(it)
                }
        }
    }

    override fun deleteTopicById(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            topicRepository.deleteTopicById(id)
                .catch { exception ->
                    topicContractView.showError(TAG_DELETE, exception.localizedMessage)
                }
                .collect {
                    topicContractView.showToast("해당 주제가 삭제되었습니다.")
                }
        }
    }
}