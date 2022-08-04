package com.w36495.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.w36495.about.fragment.TopicListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.main_fragment_container, TopicListFragment()).commit()
    }
}