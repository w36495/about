package com.w36495.about.contract

interface SettingContract {

    interface View {
        fun showToast(message: String)
        fun showErrorToast(tag: String, message: String)
    }

    interface Presenter {
        fun resetAllData()
    }

}