package com.w36495.about.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.w36495.about.databinding.ItemCommentListBinding
import com.w36495.about.domain.entity.Comment
import com.w36495.about.util.subStringForDate

class CommentListViewHolder(
    private val binding: ItemCommentListBinding
) : RecyclerView.ViewHolder(binding.root) {

    var onDeleteClick: ((RecyclerView.ViewHolder) -> Unit)? = null

    fun bind(comment: Comment) {
        itemView.apply {
            comment.registDate.apply {
                binding.thinkDetailItemDate.text = this.subStringForDate(this)
            }
            binding.thinkDetailItemContent.text = comment.comment
            binding.thinkDetailRemove.setOnClickListener {
                onDeleteClick?.let { onDeleteClick ->
                    onDeleteClick(this@CommentListViewHolder)
                }
            }

            this.setOnClickListener {
                if (this.scrollX != 0) {
                    this.scrollTo(0, 0)
                }
                Snackbar.make(it, "${comment.comment} Click!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    fun getWidthOfContainer(): Int {
        return binding.thinkDetailRemoveContainer.width
    }
}
