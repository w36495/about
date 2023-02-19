package com.w36495.about.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.w36495.about.R
import com.w36495.about.ui.fragment.TopicListFragment

class SettingActivity : AppCompatActivity() {

    private lateinit var resetLayout: LinearLayout
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        toolbar = findViewById(R.id.setting_toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        resetLayout = findViewById(R.id.setting_reset_layout)
        resetLayout.setOnClickListener {
            showResetDialog()
        }

    }

    private fun showResetDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.dialog_reset_title))
            .setMessage(resources.getString(R.string.dialog_reset_text))
            .setNeutralButton(resources.getString(R.string.dialog_btn_cancel)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.dialog_btn_check)) { dialog, _ ->
                val moveTopicList = Intent(this, MainActivity::class.java)
                setResult(TopicListFragment.INTENT_RESULT_RESET, moveTopicList)
                finish()
            }
            .show()
    }

}