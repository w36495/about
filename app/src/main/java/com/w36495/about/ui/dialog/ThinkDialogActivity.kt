package com.w36495.about.ui.dialog

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.w36495.about.R
import com.w36495.about.ui.fragment.ThinkListFragment

class ThinkDialogActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var dialogToolbar: MaterialToolbar
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    private lateinit var inputText: EditText

    private var position: Int? = null

    private val size = Point()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_think_dialog)

        initDialog()

        position?.let {
            dialogToolbar.title = "${it+1}번째 생각"
        }

        val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        display.getSize(size)
    }

    private fun initDialog() {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogToolbar = findViewById(R.id.dialog_think_add_toolbar)
        cancelButton = findViewById(R.id.dialog_think_add_btn_cancel)
        saveButton = findViewById(R.id.dialog_think_add_btn_save)
        inputText = findViewById(R.id.dialog_think_add_input)

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
            R.id.dialog_think_add_btn_save -> {
                val moveThinkListIntent = Intent(this, ThinkListFragment::class.java)
                moveThinkListIntent.putExtra("think", inputText.text.toString())
                setResult(ThinkListFragment.DIALOG_ADD_RESULT_CODE, moveThinkListIntent)
                finish()
            }

            R.id.dialog_think_add_btn_cancel -> {
                finish()
            }
        }
    }
}