package com.w36495.about.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.w36495.about.R
import com.w36495.about.ui.fragment.TopicListFragment

class MainActivity : AppCompatActivity() {

    private val TOPIC_LIST_TAG: String = "TOPIC_LIST_FRAGMENT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .addToBackStack(TOPIC_LIST_TAG)
            .replace(R.id.main_fragment_container, TopicListFragment())
            .commit()
    }
}