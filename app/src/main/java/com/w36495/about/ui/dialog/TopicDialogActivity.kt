package com.w36495.about.ui.dialog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.w36495.about.R
import com.w36495.about.domain.entity.Topic
import com.w36495.about.ui.fragment.TopicListFragment
import com.w36495.about.util.DateFormat
import com.w36495.about.util.StringFormat

class TopicDialogActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var dialogToolbar: MaterialToolbar
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    private lateinit var inputText: EditText

    private val size = Point()
    private var selectedColor: String = "#DCDCDC"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_topic_dialog)

        val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        display.getSize(size)
        initDialog()
    }

    private fun initDialog() {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogToolbar = findViewById(R.id.activity_topic_add_toolbar)
        cancelButton = findViewById(R.id.activity_topic_add_btn_cancel)
        saveButton = findViewById(R.id.activity_topic_add_btn_save)
        inputText = findViewById(R.id.activity_topic_add_input)

        cancelButton.setOnClickListener(this)
        saveButton.setOnClickListener(this)

        dialogToolbar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.dialog_add_menu_close -> {
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams? = window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.9).toInt()
        window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.activity_topic_add_btn_save -> {
                val topic =
                    Topic(inputText.text.toString(), selectedColor, DateFormat.currentDateFormat())

                if (StringFormat.checkLengthOfTopic(topic.topic.length)) {
                    val moveTopicList = Intent(this, TopicListFragment::class.java)
                    moveTopicList.putExtra("topic", topic.topic)
                    moveTopicList.putExtra("color", topic.color)
                    moveTopicList.putExtra("date", topic.registDate)
                    setResult(Activity.RESULT_OK, moveTopicList)
                    finish()
                } else {
                    Toast.makeText(this, "저장에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
                finish()
            }

            R.id.activity_topic_add_btn_cancel -> {
                finish()
            }
        }
    }
}