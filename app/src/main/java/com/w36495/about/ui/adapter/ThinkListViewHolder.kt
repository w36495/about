package com.w36495.about.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.w36495.about.databinding.ItemThinkListBinding
import com.w36495.about.domain.entity.Think

class ThinkListViewHolder(
    private val binding: ItemThinkListBinding
) : RecyclerView.ViewHolder(binding.root) {

    var onSwipeItem: ((RecyclerView.ViewHolder) -> Unit)? = null

    fun bind(think: Think) {
        binding.thinkListItemNumber.text = (adapterPosition + 1).toString()
        binding.thinkListItemText.text = think.text

        binding.thinkListItemDeleteContainer.setOnClickListener {
            onSwipeItem?.let { onSwipeItem ->
                onSwipeItem(this@ThinkListViewHolder)
            }
        }
    }

    fun getWidthOfContainer(): Int {
        return binding.thinkListItemDeleteContainer.width
    }

}