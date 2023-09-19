package com.w36495.about.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.w36495.about.databinding.ItemTopicListBinding
import com.w36495.about.domain.dto.TopicListDTO
import com.w36495.about.util.DateFormat

class TopicListViewHolder(private val binding: ItemTopicListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var onClickItem: ((TopicListViewHolder) -> Unit)? = null
    var onLongClickItem: ((TopicListViewHolder) -> Unit)? = null

    fun bind(topic: TopicListDTO) {
        binding.topicListItemTopic.text = topic.topic
        binding.topicListItemDate.text = DateFormat.calDate(topic.registDate).toString()
        binding.topicListItemThinkCount.text = topic.countOfThink.toString()

        binding.topicListItemCardView.setOnClickListener {
            onClickItem?.let { onClickItem ->
                onClickItem(this@TopicListViewHolder)
            }
        }

        binding.topicListItemCardView.setOnLongClickListener {
            onLongClickItem?.let { onLongClickItem ->
                onLongClickItem(this@TopicListViewHolder)
                true
            }
            false
        }
    }
}