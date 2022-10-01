package com.w36495.about.ui.dialog

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.appbar.MaterialToolbar
import com.w36495.about.R
import com.w36495.about.domain.entity.Topic
import com.w36495.about.ui.listener.TopicDialogClickListener
import com.w36495.about.util.currentDateFormat

class TopicAddDialog(
    private val size: Point,
    private val topicDialogClickListener: TopicDialogClickListener
) :
    DialogFragment(), View.OnClickListener {

    private lateinit var dialogToolbar: MaterialToolbar
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    private lateinit var inputText: EditText
    private lateinit var colorSelectButton: Button

    private var selectedColor: String = "#DCDCDC"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_topic_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogToolbar = view.findViewById(R.id.dialog_topic_add_toolbar)
        cancelButton = view.findViewById(R.id.dialog_topic_add_btn_cancel)
        saveButton = view.findViewById(R.id.dialog_topic_add_btn_save)
        inputText = view.findViewById(R.id.dialog_topic_add_input)

        cancelButton.setOnClickListener(this)
        saveButton.setOnClickListener(this)
        colorSelectButton.setOnClickListener(this)

        dialogToolbar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.dialog_add_menu_close -> {
                    dismiss()
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = true
    }

    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.dialog_topic_add_btn_save -> {
                val topic =
                    Topic(inputText.text.toString(), selectedColor, currentDateFormat())
                if (topicDialogClickListener.onTopicSaveClicked(topic)) {
                    dismiss()
                } else {
                    topicDialogClickListener.onErrorTopicSaved()
                }
            }

            R.id.dialog_topic_add_btn_cancel -> {
                dismiss()
            }
        }
    }
}