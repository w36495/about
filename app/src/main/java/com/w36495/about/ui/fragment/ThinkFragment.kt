package com.w36495.about.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.w36495.about.contract.ThinkContract
import com.w36495.about.data.CommentUiState
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
import com.w36495.about.util.subStringForDate
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ThinkFragment(
    private val position: Int,
    private val think: Think
) : Fragment(), ThinkContract.View, CommentItemClickListener, CommentItemDeleteListener {

    private var _binding: FragmentThinkBinding? = null
    private val binding: FragmentThinkBinding get() = _binding!!
    private val commentListAdapter: CommentListAdapter by lazy {
        CommentListAdapter()
    }

    private var database: AppDatabase? = null
    private val presenter: ThinkContract.Presenter by lazy {
        ThinkPresenter(
            thinkRepository = ThinkRepositoryImpl((database!!.thinkDao())),
            commentRepository = CommentRepositoryImpl(database!!.commentDao()),
            commentView = this
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
        binding.thinkToolbar.title = "${position}번째 생각"
        binding.thinkContents.apply {
            setText(think.text)
        }
        think.registDate.apply {
            binding.thinkDate.text = this.subStringForDate(this)
        }

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
        }

        commentListAdapter.setOnCommentItemClickListener(this)
    }

    private fun getContentFromPresenter() {
        with(presenter) {
            getAllCommentList(think.id)
            getCountCommentList(think.id)
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
            thinkId = think.id,
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
        presenter.deleteComment(commentId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}