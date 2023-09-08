package com.w36495.about.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.w36495.about.databinding.DialogCommentBinding
import com.w36495.about.ui.listener.CommentItemDeleteListener

class CommentBottomSheetDialogFragment(
    private val commentId: Long
) : BottomSheetDialogFragment() {

    private var _binding: DialogCommentBinding? = null
    private val binding: DialogCommentBinding get() = _binding!!
    private lateinit var commentItemDeleteListener: CommentItemDeleteListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogCommentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCommentDelete.setOnClickListener {
            commentItemDeleteListener.onDeleteItem(commentId)
            dismiss()
        }

        binding.btnCommentCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setOnCommentItemDeleteListener(listener: CommentItemDeleteListener) {
        this.commentItemDeleteListener = listener
    }
}