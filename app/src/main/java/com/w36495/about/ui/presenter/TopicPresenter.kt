package com.w36495.about.ui.presenter

import com.w36495.about.R
import com.w36495.about.domain.entity.Topic
import com.w36495.about.contract.TopicContract
import com.w36495.about.data.TopicUiState
import com.w36495.about.data.repository.ThinkRepositoryImpl
import com.w36495.about.data.repository.TopicRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TopicPresenter(
    private val topicRepository: TopicRepositoryImpl,
    private val thinkRepository: ThinkRepositoryImpl,
    private val topicContractView: TopicContract.View
) : TopicContract.Presenter {

    private val _uiState = MutableStateFlow<TopicUiState>(TopicUiState.Loading)
    val uiState: StateFlow<TopicUiState> = _uiState.asStateFlow()

    override fun saveTopic(topic: Topic) {
        CoroutineScope(Dispatchers.IO).launch {
            topicRepository.saveTopic(topic)
                .catch { exception ->
                    topicContractView.showError(R.string.tag_topic_insert.toString(), exception.localizedMessage)
                }
                .collect {
                    topicContractView.showToast("새로운 주제가 등록되었습니다.")
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
                    _uiState.value = TopicUiState.Failed(exception.localizedMessage)
                }
                .collect {
                    it.forEach { topic ->
                        thinkRepository.getThinkListSize(topic.id)
                            .catch { exception ->
                                topicContractView.showError(R.string.tag_think_list_size.toString(), exception.localizedMessage)
                            }
                            .collect {
                                topic.count = it
                            }
                    }
                    _uiState.value = TopicUiState.Success(it)
                }
        }
    }

    override fun deleteAllTopic() {
        CoroutineScope(Dispatchers.IO).launch {
            var topicListCount = 0
            var thinkListCount = 0
            topicRepository.getTopicListCount()
                .catch { exception ->
                    printError("topicRepository.getTopicListCount()", exception.localizedMessage)
                }
                .collect {
                    topicListCount = it
                }
            thinkRepository.getThinkListCount()
                .catch { exception ->
                    printError("thinkRepository.getThinkListCount()", exception.localizedMessage)
                }
                .collect {
                    thinkListCount = it
                }

            if (topicListCount == 0) {
                topicContractView.showError(R.string.tag_topic_list_empty.toString(), "등록된 주제가 없습니다.")
            } else {
                topicRepository.deleteAllTopic()
                    .catch { exception ->
                        printError("topicRepository.deleteAllTopic()", exception.localizedMessage)
                    }
                    .collect {
                        if (thinkListCount != 0) {
                            thinkRepository.deleteAllThink()
                                .catch { exception ->
                                    printError("thinkRepository.deleteAllThink()", exception.localizedMessage)
                                }
                                .collect ()
                        }
                        topicContractView.showEmptyTopicList()
                    }
            }
        }
    }

    override fun deleteTopicById(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            topicRepository.deleteTopicById(id)
                .catch { exception ->
                    topicContractView.showError(R.string.tag_topic_delete.toString(), exception.localizedMessage)
                }
                .collect {
                    topicContractView.showToast("해당 주제가 삭제되었습니다.")
                }
        }
    }

    override fun deleteThinkListByTopicId(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            thinkRepository.deleteThinkByTopicId(id)
                .catch { exception ->
                    topicContractView.showError(R.string.tag_topic_delete.toString(), exception.localizedMessage)
                }
                .collect()
        }
    }

    private fun printError(tag: String, error: String) {
        println("===== $tag : $error =====")
    }
}