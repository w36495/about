package com.w36495.about.ui.presenter

import com.w36495.about.contract.SettingContract
import com.w36495.about.data.repository.TopicRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingPresenter(
    private val topicRepository: TopicRepositoryImpl,
    private val settingView: SettingContract.View
) : SettingContract.Presenter {

    override fun resetAllData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                topicRepository.deleteAllTopic()
                settingView.showToast("초기화가 완료되었습니다.")
            } catch (exception: Exception) { }
        }
    }
}