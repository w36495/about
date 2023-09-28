package com.w36495.about.ui.presenter

import com.w36495.about.contract.ThinkContract
import com.w36495.about.data.CommentUiState
import com.w36495.about.data.ThinkUiState
import com.w36495.about.data.repository.CommentRepositoryImpl
import com.w36495.about.data.repository.ThinkRepositoryImpl
import com.w36495.about.domain.entity.Comment
import com.w36495.about.domain.entity.Think
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThinkPresenter(
    private val thinkRepository: ThinkRepositoryImpl,
    private val commentRepository: CommentRepositoryImpl,
    private val thinkView: ThinkContract.View,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) : ThinkContract.Presenter {

    private val _thinkUiState = MutableStateFlow<ThinkUiState>(ThinkUiState.Loading)
    val thinkUiState: StateFlow<ThinkUiState> get() = _thinkUiState

    private val _uiState = MutableStateFlow<CommentUiState>(CommentUiState.Loading)
    val uiState: StateFlow<CommentUiState> = _uiState

    private val _commentState = MutableStateFlow(0)
    val commentState: StateFlow<Int> = _commentState

    override fun getThink(thinkId: Long) {
        // TODO : THINK_PRESENTER getThink()
    }

    override fun updateThink(thinkId: Long, think: String, updateDate: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                thinkRepository.updateThink(thinkId, think, updateDate)
                withContext(mainDispatcher) {
                    thinkView.showToast("생각 수정이 완료되었습니다.")
                }
            } catch (exception: Exception) {
                thinkView.showErrorToast("생각 수정 중 오류가 발생하였습니다.")
            }
        }
    }

    override fun deleteThink(thinkId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                thinkRepository.deleteThinkById(thinkId)
                withContext(mainDispatcher) {
                    thinkView.showToast("생각이 삭제되었습니다.")
                }
            } catch (exception: Exception) {
                withContext(mainDispatcher) {
                    thinkView.showErrorToast("생각 삭제 중 오류가 발생하였습니다.")
                }
            }
        }
    }

    override fun getAllCommentList(thinkId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            commentRepository.getAllCommentList(thinkId)
                .catch {
                    _uiState.value = CommentUiState.Failed(it.localizedMessage)
                }
                .collect {
                    _uiState.value = CommentUiState.Success(it)
                }
        }
    }

    override fun insertComment(comment: Comment) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                commentRepository.insertComment(comment)
                withContext(mainDispatcher) {
                    thinkView.showToast("코멘트가 등록되었습니다.")
                }
            } catch (exception: Exception) {
                withContext(mainDispatcher) {
                    thinkView.showErrorToast("코멘트 등록 중 오류가 발생하였습니다.")
                }
            }
        }
    }

    override fun deleteComment(commentId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                commentRepository.deleteCommentById(commentId)
                withContext(mainDispatcher) {
                    thinkView.showToast("코멘트가 삭제되었습니다.")
                }
            } catch (exception: Exception) {
                withContext(mainDispatcher) {
                    thinkView.showErrorToast("코멘트 삭제 중 오류가 발생하였습니다.")
                }
            }
        }
    }

    override fun getCountCommentList(thinkId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            commentRepository.getCountCommentList(thinkId)
                .collectLatest {
                    _commentState.value = it
                }
        }
    }
}