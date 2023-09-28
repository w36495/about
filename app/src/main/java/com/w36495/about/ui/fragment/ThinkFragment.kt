package com.w36495.about.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.w36495.about.R
import com.w36495.about.contract.ThinkContract
import com.w36495.about.data.CommentUiState
import com.w36495.about.data.ThinkUiState
import com.w36495.about.data.local.AppDatabase
import com.w36495.about.data.repository.CommentRepositoryImpl
import com.w36495.about.data.repository.ThinkRepositoryImpl
import com.w36495.about.databinding.FragmentThinkBinding
import com.w36495.about.domain.entity.Comment
import com.w36495.about.domain.entity.Think
import com.w36495.about.ui.adapter.CommentListAdapter
import com.w36495.about.ui.listener.CommentItemClickListener
import com.w36495.about.ui.listener.CommentItemDeleteListener
import com.w36495.about.ui.listener.CommentItemTouchHelper
import com.w36495.about.ui.presenter.ThinkPresenter
import com.w36495.about.util.DateFormat
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ThinkFragment : Fragment(), ThinkContract.View, CommentItemClickListener, CommentItemDeleteListener {

    private var _binding: FragmentThinkBinding? = null
    private val binding: FragmentThinkBinding get() = _binding!!

    private val args: ThinkFragmentArgs by navArgs()

    private val commentListAdapter: CommentListAdapter by lazy {
        CommentListAdapter()
    }

    private var database: AppDatabase? = null
    private val presenter: ThinkContract.Presenter by lazy {
        ThinkPresenter(
            thinkRepository = ThinkRepositoryImpl((database!!.thinkDao())),
            commentRepository = CommentRepositoryImpl(database!!.commentDao()),
            thinkView = this
        )
    }

    private lateinit var commentItemTouchHelper: CommentItemTouchHelper
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThinkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = AppDatabase.getInstance(view.context)
        setup()
    }

    private fun setup() {
        binding.thinkToolbar.title = "${args.position + 1}번째 생각"
        binding.thinkContents.apply {
            setText(args.think.think)
        }
        binding.thinkDate.text = args.think.registDate.substring(0, 10)
        binding.thinkToolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.thinkCommentInputBtn.setOnClickListener {
            val inputComment = binding.thinkCommentInput.text.toString()
            saveComment(inputComment)
            binding.thinkCommentInput.text.clear()
        }

        setupRecyclerView()
        getContentFromPresenter()

        showThink(args.thinkId)
        showCommentCount()
        showCommentList()
    }

    private fun setupRecyclerView() {
        commentItemTouchHelper = CommentItemTouchHelper()
        itemTouchHelper = ItemTouchHelper(commentItemTouchHelper)
        itemTouchHelper.attachToRecyclerView(binding.thinkRecyclerview)

        binding.thinkRecyclerview.apply {
            adapter = commentListAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setOnTouchListener { _, _ ->
                commentItemTouchHelper.removePreviousSwipe(this)
                false
            }
        }

        commentListAdapter.setOnCommentItemClickListener(this)
    }

    private fun showThink(thinkId: Long) {
        presenter.getThink(thinkId)

        lifecycleScope.launch {
            (presenter as ThinkPresenter).thinkUiState.collectLatest {
                when(it) {
                    is ThinkUiState.Loading -> { }
                    is ThinkUiState.Success -> {
                        if(it.think == null) {
                            binding.root.findNavController().popBackStack()
                        } else {
                            binding.thinkToolbar.title = "${args.position + 1}번째 생각"
                            binding.thinkContents.apply {
                                setText(it.think.think)
                            }
                            binding.thinkDate.text = it.think.registDate.substring(0, 10)

                            showMultiDialog(it.think)
                        }
                    }
                    is ThinkUiState.Failed -> { }
                }
            }
        }
    }

    private fun showMultiDialog(think: Think) {
        binding.thinkToolbar.setOnMenuItemClickListener { menu ->
            when(menu.itemId) {
                R.id.menu_think_detail_more -> {
                    val menus = arrayOf(resources.getString(R.string.dialog_menu_think_update), resources.getString(R.string.dialog_menu_think_delete))
                    MaterialAlertDialogBuilder(requireContext())
                        .setItems(menus) { dialog, item ->
                            when(item) {
                                0 -> {
                                    updateThink()
                                }
                                1 -> {
                                    MaterialAlertDialogBuilder(requireContext(), R.style.alert_dialog_delete_style)
                                        .setTitle(resources.getString(R.string.dialog_title_delete))
                                        .setMessage("[${think.think}] 생각에 해당되는 모든 코멘트들도 함께 삭제되며, 복구는 불가능합니다.")
                                        .setNeutralButton(resources.getString(R.string.dialog_btn_cancel)) { dialog, _ ->
                                            dialog.dismiss()
                                        }
                                        .setPositiveButton(resources.getString(R.string.dialog_btn_delete)) { dialog, _ ->
                                            presenter.deleteThink(think.id)
                                        }
                                        .show()
                                }
                            }
                        }.show()
                    true
                }
                else -> false
            }
        }
    }

    private fun updateThink() {
        binding.thinkContents.apply {
            isEnabled = true
            requestFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            // TODO : toggleSoftInput -> API 31 에서 deprecated -> 버전별 처리하기
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
        binding.thinkToolbar.menu[0].icon = resources.getDrawable(R.drawable.ic_baseline_add_24)
        binding.thinkToolbar.setOnMenuItemClickListener { menu ->
            when(menu.itemId) {
                R.id.menu_think_detail_more -> {
                    binding.thinkContents.isEnabled = false
                    binding.thinkToolbar.menu[0].icon = resources.getDrawable(R.drawable.ic_more_vert_24)

                    val inputText = binding.thinkContents.text.toString()
                    presenter.updateThink(args.thinkId, inputText, DateFormat.currentDateFormat())
                    true
                }
                else -> false
            }
        }
    }

    private fun setupComment(thinkId: Long) {
        with(presenter) {
            getAllCommentList(args.think.id)
            getCountCommentList(args.think.id)
        }
    }

    private fun showCommentCount() {
        lifecycleScope.launch {
            (presenter as ThinkPresenter).commentState.collectLatest {
                binding.tvCommentCount.text = "코멘트(${it})"
            }
        }
    }

    private fun showCommentList() {
        lifecycleScope.launch {
            (presenter as ThinkPresenter).uiState.collectLatest { uiState ->
                when (uiState) {
                    is CommentUiState.Loading -> {
                        // TODO : COMMENT_UI_LOADING PROGRESSBAR
                    }

                    is CommentUiState.Empty -> {
                        commentListAdapter.clearCommentList()
                    }

                    is CommentUiState.Success -> {
                        commentListAdapter.setupCommentList(uiState.thinkList)
                    }

                    is CommentUiState.Failed -> {
                        // TODO : COMMENT_UI_FAILED
                    }
                }
            }
        }
    }

    private fun saveComment(inputText: String) {
        val comment = Comment(
            thinkId = args.think.id,
            comment = inputText,
            registDate = DateFormat.currentDateFormat()
        )

        presenter.insertComment(comment)
    }

    override fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onClickItem(commentId: Long) {
        val commentBottomDialog = CommentBottomSheetDialogFragment(commentId)
        commentBottomDialog.setOnCommentItemDeleteListener(this)
        commentBottomDialog.show(parentFragmentManager, "COMMENT_BOTTOM_DIALOG")
    }

    override fun onDeleteItem(commentId: Long) {
        commentItemTouchHelper.removeSwipeAfterDelete(binding.thinkRecyclerview)
        presenter.deleteComment(commentId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}