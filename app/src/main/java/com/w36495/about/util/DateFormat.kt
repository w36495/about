package com.w36495.about.util

import java.text.SimpleDateFormat
import java.util.*

private val current = System.currentTimeMillis()
private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

fun currentDateFormat(): String {
    val date = Date(current)
    return dateFormat.format(date)
}

fun calDate(topicDate: String): Long {

    val current = System.currentTimeMillis()
    val currentDate = Date(current)

    val calculatorMillis = currentDate.time - dateFormat.parse(topicDate).time

    return calculatorMillis / (60 * 60 * 24 * 1000)
}