package com.w36495.about.dialog

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
import com.w36495.about.data.Think
import com.w36495.about.listener.ThinkDialogClickListener
import com.w36495.about.util.currentDateFormat

class ThinkUpdateDialog(
    private val position: Int,
    private val think: Think,
    private val size: Point,
    private val thinkDialogClickListener: ThinkDialogClickListener
) :
    DialogFragment(), View.OnClickListener {

    private lateinit var dialogToolbar: MaterialToolbar
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    private lateinit var inputText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_think_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogToolbar = view.findViewById(R.id.dialog_think_add_toolbar)
        cancelButton = view.findViewById(R.id.dialog_think_add_btn_cancel)
        saveButton = view.findViewById(R.id.dialog_think_add_btn_save)
        inputText = view.findViewById(R.id.dialog_think_add_input)

        cancelButton.setOnClickListener(this)
        saveButton.setOnClickListener(this)

        dialogToolbar.title = "${position + 1}번째 생각"
        inputText.setText(think.text)

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
            R.id.dialog_think_add_btn_save -> {
                think.text = inputText.text.toString()
                think.updateDate = currentDateFormat()
                thinkDialogClickListener.onThinkUpdateClicked(think)
                dismiss()
            }

            R.id.dialog_think_add_btn_cancel -> {
                dismiss()
            }
        }
    }
}