package com.w36495.about.ui.presenter

import com.w36495.about.domain.entity.Think
import com.w36495.about.contract.ThinkListContract
import com.w36495.about.data.ThinkListUiState
import com.w36495.about.data.repository.ThinkRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ThinkListPresenter(
    private val thinkRepository: ThinkRepositoryImpl,
    private val thinkListView: ThinkListContract.View
) : ThinkListContract.Presenter {

    private val _uiState = MutableStateFlow<ThinkListUiState>(ThinkListUiState.Loading)
    val uiState: StateFlow<ThinkListUiState> = _uiState.asStateFlow()

    override fun getThinkList(topicId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            thinkRepository.getThinkListByTopicId(topicId)
                .onStart {
                    _uiState.value = ThinkListUiState.Loading
                }
                .catch { exception ->
                    _uiState.value = ThinkListUiState.Failed(exception.localizedMessage)
                }
                .collect {
                    _uiState.value = ThinkListUiState.Success(it)
                }
        }
    }

    override fun saveThink(think: Think) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                thinkRepository.insertThink(think)
            } catch (exception: Exception) { }
        }
    }

    override fun deleteThinkById(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                thinkRepository.deleteThinkById(id)
            } catch (exception: Exception) { }
        }
    }
}