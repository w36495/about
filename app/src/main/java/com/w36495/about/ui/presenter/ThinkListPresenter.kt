package com.w36495.about.ui.presenter

import com.w36495.about.domain.entity.Think
import com.w36495.about.contract.ThinkListContract
import com.w36495.about.data.ThinkUiState
import com.w36495.about.data.repository.ThinkRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ThinkListPresenter(
    private val thinkRepository: ThinkRepositoryImpl,
    private val thinkListView: ThinkListContract.View
) : ThinkListContract.Presenter {

    private val _uiState = MutableStateFlow<ThinkUiState>(ThinkUiState.Loading)
    val uiState: StateFlow<ThinkUiState> = _uiState.asStateFlow()

    override fun getThink(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            thinkRepository.getThink(id)
                .catch { exception ->
                    _uiState.value = ThinkUiState.Failed(exception.localizedMessage)
                }
                .collect {
                    thinkListView.showThink(it)
                }
        }
    }

    override fun getThinkList(topicId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            thinkRepository.getThinkList(topicId)
                .onStart {
                    _uiState.value = ThinkUiState.Loading
                }
                .catch { exception ->
                    _uiState.value = ThinkUiState.Failed(exception.localizedMessage)
                }
                .collect {
                    _uiState.value = ThinkUiState.Success(it)
                }
        }
    }

    override fun saveThink(think: Think) {
        CoroutineScope(Dispatchers.IO).launch {
            thinkRepository.saveThink(think)
                .catch { exception ->
                    thinkListView.showError(exception.localizedMessage)
                }
                .collect {
                    thinkListView.showToast("새로운 생각이 등록되었습니다.")
                }
        }
    }

    override fun updateThink(think: Think) {
        CoroutineScope(Dispatchers.IO).launch {
            thinkRepository.updateThink(think)
                .catch { exception ->
                    exception.localizedMessage?.let { thinkListView.showError(it) }
                }
                .collect {
                    thinkListView.showToast("생각이 수정되었습니다.")
                }
        }
    }

    override fun deleteThinkById(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            thinkRepository.deleteThinkById(id)
                .catch { exception ->
                    exception.localizedMessage?.let { thinkListView.showError(it) }
                }
                .collect {
                    thinkListView.showToast("생각이 삭제되었습니다.")
                }
        }
    }
}