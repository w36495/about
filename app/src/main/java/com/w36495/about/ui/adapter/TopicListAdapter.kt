package com.w36495.about.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.w36495.about.ui.listener.TopicListClickListener
import com.w36495.about.databinding.ItemTopicListBinding
import com.w36495.about.domain.dto.TopicListDTO

class TopicListAdapter : RecyclerView.Adapter<TopicListViewHolder>() {

    private var topicList = mutableListOf<TopicListDTO>()
    private lateinit var topicListClickListener: TopicListClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicListViewHolder {
        return TopicListViewHolder(ItemTopicListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TopicListViewHolder, position: Int) {
        holder.bind(topicList[position])

        holder.onClickItem = {
            topicListClickListener.onClickItem(topicList[position])
        }
        holder.onLongClickItem = {
            topicListClickListener.onLongClickItem(topicList[position])
        }
    }

    override fun getItemCount(): Int = topicList.size

    fun setClickListener(topicListClickListener: TopicListClickListener) {
        this.topicListClickListener = topicListClickListener
    }

    fun setTopicList(topicList: List<TopicListDTO>) {
        this.topicList = topicList as ArrayList<TopicListDTO>
        notifyDataSetChanged()
    }

    fun setTopicEmptyList() {
        this.topicList.clear()
        notifyDataSetChanged()
    }
}