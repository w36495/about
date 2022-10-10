package com.w36495.about.util

class StringFormat {
    companion object {
        fun checkLengthOfTopic(lengthOfTopic: Int): Boolean {
            return lengthOfTopic in 1..30
        }
    }
}