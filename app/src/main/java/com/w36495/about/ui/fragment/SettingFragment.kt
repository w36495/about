package com.w36495.about.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.w36495.about.R
import com.w36495.about.contract.SettingContract
import com.w36495.about.data.local.AppDatabase
import com.w36495.about.data.repository.TopicRepositoryImpl
import com.w36495.about.databinding.FragmentSettingBinding
import com.w36495.about.ui.presenter.SettingPresenter

class SettingFragment : Fragment(), SettingContract.View {

    private var _binding: FragmentSettingBinding? = null
    private val binding: FragmentSettingBinding get() = _binding!!
    private var navController: NavController? = null

    private var appDatabase: AppDatabase? = null
    private lateinit var settingPresenter: SettingContract.Presenter

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

        setupDatabase()
        navController = view.findNavController()

        binding.settingResetLayout.setOnClickListener {
            showResetDialog(view)
        }
    }

    private fun setupDatabase() {
        appDatabase = AppDatabase.getInstance(requireContext())
        settingPresenter = SettingPresenter(TopicRepositoryImpl(appDatabase!!.topicDao()), this)
    }

    private fun showResetDialog(view: View) {
        MaterialAlertDialogBuilder(view.context)
            .setTitle(resources.getString(R.string.dialog_reset_title))
            .setMessage(resources.getString(R.string.dialog_reset_text))
            .setNeutralButton(resources.getString(R.string.dialog_btn_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.dialog_btn_check)) { _, _ ->
                settingPresenter.resetAllData()
                navController?.let {
                    it.navigate(R.id.nav_topicList_fragment)
                }
            }
            .show()
    }

    override fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorToast(tag: String, message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        navController = null
        appDatabase = null
    }
}