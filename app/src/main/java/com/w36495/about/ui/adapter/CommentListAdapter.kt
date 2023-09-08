package com.w36495.about.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.w36495.about.databinding.ItemCommentListBinding
import com.w36495.about.domain.entity.Comment
import com.w36495.about.ui.listener.CommentItemClickListener

class CommentListAdapter : RecyclerView.Adapter<CommentListViewHolder>() {

    private var commentList: ArrayList<Comment> = arrayListOf()
    private lateinit var commentItemClickListener: CommentItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentListViewHolder {
        return CommentListViewHolder(
            ItemCommentListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CommentListViewHolder, position: Int) {
        commentList.let { list ->
            holder.bind(list[position])
        }

        holder.onDeleteClick = {
            commentItemClickListener.onItemClicked(commentList[it.adapterPosition].id)
        }
    }

    override fun getItemCount(): Int =
        commentList.let { list ->
            list.size
        }

    fun setOnCommentItemClickListener(listener: CommentItemClickListener) {
        this.commentItemClickListener = listener
    }

    fun setupCommentList(list: List<Comment>) {
        this.commentList = list as ArrayList<Comment>
        notifyDataSetChanged()
    }

    fun clearCommentList() {
        commentList.clear()
        notifyDataSetChanged()
    }
}