package com.w36495.about.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.w36495.about.R
import com.w36495.about.databinding.FragmentSettingBinding
import com.w36495.about.ui.listener.ResetClickListener

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding: FragmentSettingBinding get() = _binding!!

    private var resetClickListener: ResetClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingResetLayout.setOnClickListener {
            showResetDialog(view)
        }
    }

    private fun showResetDialog(view: View) {
        MaterialAlertDialogBuilder(view.context)
            .setTitle(resources.getString(R.string.dialog_reset_title))
            .setMessage(resources.getString(R.string.dialog_reset_text))
            .setNeutralButton(resources.getString(R.string.dialog_btn_cancel)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.dialog_btn_check)) { _, _ ->
                resetClickListener?.let {
                    it.onResetClicked()
                    parentFragmentManager.commit {
                        replace<TopicListFragment>(R.id.main_fragment_container)
                        setReorderingAllowed(true)
                    }
                }
            }
            .show()
    }

    fun setOnResetClickListener(listener: ResetClickListener) {
        this.resetClickListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        resetClickListener = null
    }
}