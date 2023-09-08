package com.w36495.about.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.commit
import com.w36495.about.R
import com.w36495.about.ui.fragment.TopicListFragment

class MainActivity : AppCompatActivity() {

    private val TOPIC_LIST_TAG: String = "TOPIC_LIST_FRAGMENT"
    private lateinit var splashScreen: SplashScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSplashScreen()
        setContentView(R.layout.activity_main)
        initView(savedInstanceState)
    }

    private fun initSplashScreen() {
        splashScreen = installSplashScreen()
    }

    private fun initView(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.main_fragment_container, TopicListFragment())
            }
        }
    }
}