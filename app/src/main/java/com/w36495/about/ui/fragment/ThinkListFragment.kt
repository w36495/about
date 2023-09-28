package com.w36495.about.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.w36495.about.ui.listener.ThinkItemTouchHelper
import com.w36495.about.R
import com.w36495.about.ui.adapter.ThinkListAdapter
import com.w36495.about.domain.entity.Think
import com.w36495.about.contract.ThinkListContract
import com.w36495.about.data.ThinkListUiState
import com.w36495.about.ui.presenter.ThinkListPresenter
import com.w36495.about.domain.entity.Topic
import com.w36495.about.data.local.AppDatabase
import com.w36495.about.data.repository.ThinkRepositoryImpl
import com.w36495.about.databinding.FragmentThinkListBinding
import com.w36495.about.ui.dialog.ThinkDialogActivity
import com.w36495.about.ui.listener.ThinkListItemClickListener
import com.w36495.about.ui.listener.ThinkSwipeListener
import com.w36495.about.util.DateFormat
import kotlinx.coroutines.launch

class ThinkListFragment : Fragment(), ThinkListItemListener, ThinkListContract.View {

    private var _binding: FragmentThinkListBinding? = null
    private val binding: FragmentThinkListBinding get() = _binding!!

    private val args: ThinkListFragmentArgs by navArgs()

    companion object {
        const val DIALOG_ADD_TAG: String = "THINK_ADD_TAG"

        const val DIALOG_ADD_RESULT_CODE: Int = 999
    }

    private lateinit var getResultThink: ActivityResultLauncher<Intent>

    private lateinit var thinkListAdapter: ThinkListAdapter
    private lateinit var thinkItemTouchHelper: ThinkItemTouchHelper

    private var database: AppDatabase? = null
    private lateinit var presenter: ThinkListContract.Presenter

    private var position: Int? = null
    private var currentThink: Think? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThinkListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thinkListContext = view.context
        thinkListView = view

        database = AppDatabase.getInstance(view.context)

        presenter = ThinkListPresenter(ThinkRepositoryImpl(database!!.thinkDao()), this)

        binding.thinkListToolbar.title = topic.topic

        thinkListAdapter = ThinkListAdapter()
        thinkListAdapter.setClickListener(this, this)

        thinkItemTouchHelper = ThinkItemTouchHelper()
        val itemTouchHelper = ItemTouchHelper(thinkItemTouchHelper)

        binding.thinkListToolbar.title = args.topic.topic

        binding.thinkListRecyclerview.apply {
            adapter = thinkListAdapter
            layoutManager = LinearLayoutManager(view.context)

            itemTouchHelper.attachToRecyclerView(this)
            setOnTouchListener { _, _ ->
                thinkItemTouchHelper.removePreviousSwipe(this)
                false
            }
        }

        getResultThink = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == DIALOG_ADD_RESULT_CODE) {
                result.data?.let {
                    val think = Think(
                        topicId = args.topic.id,
                        think = it.getStringExtra("think")!!,
                        registDate = DateFormat.currentDateFormat(),
                        updateDate = DateFormat.currentDateFormat()
                    )
                    (presenter as ThinkListPresenter).saveThink(think)
                }
            }
        }

        binding.thinkListToolbar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.main_add -> {
                    val moveThinkAddIntent =
                        Intent(thinkListContext, ThinkDialogActivity::class.java)
                    moveThinkAddIntent.putExtra("tag", DIALOG_ADD_TAG)
                    getResultThink.launch(moveThinkAddIntent)
                    true
                }
                else -> false
            }
        }

        binding.thinkListToolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        showThinks()
    }

    private fun showThinks() {
        presenter.getThinkList(args.topic.id)

        lifecycleScope.launch {
            (presenter as ThinkListPresenter).uiState.collect { uiState ->
                when (uiState) {
                    is ThinkListUiState.Loading -> {
                        println("===== loading =====")
                    }
                    is ThinkListUiState.Empty -> {
                        println("===== empty =====")
                    }
                    is ThinkListUiState.Success -> {
                        thinkListAdapter.setThinkList(uiState.thinkList)
                    }
                    is ThinkListUiState.Failed -> {
                        println("===== failed : ${uiState.message} =====")
                    }
                }
            }
        }
    }

    override fun onThinkSwiped(thinkId: Long) {
        thinkItemTouchHelper.removeSwipeAfterDelete(binding.thinkListRecyclerview)

        MaterialAlertDialogBuilder(thinkListContext, R.style.AboutTheme_AlertDialog)
            .setTitle(resources.getString(R.string.dialog_title_think_delete))
            .setMessage(resources.getString(R.string.dialog_message_think_delete))
            .setNeutralButton(resources.getString(R.string.dialog_btn_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.dialog_btn_delete)) { dialog, _ ->
                presenter.deleteThinkById(thinkId)
            }
            .show()
    }

    override fun onThinkListItemClicked(position: Int, think: Think) {
        this.position = position
        val action = ThinkListFragmentDirections.actionThinkListFragmentToThinkFragment(think, position)
        binding.root.findNavController().navigate(action)
    }

    override fun showThinkList(thinkList: List<Think>) {
        thinkListAdapter.setThinkList(thinkList)
    }

    override fun showThink(think: Think) {
        currentThink = think

        position?.let {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                addToBackStack(THINK_DETAIL_TAG)
                replace(R.id.main_fragment_container, ThinkFragment(it+1, think))
            }
        }

    }

    override fun showToast(message: String) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable {
            Toast.makeText(thinkListContext, message, Toast.LENGTH_SHORT).show()
        }, 0)
        showThinks()
    }

    override fun showError(message: String) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable {
            Toast.makeText(thinkListContext, message, Toast.LENGTH_SHORT).show()
        }, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}