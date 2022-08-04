package com.w36495.about.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.w36495.about.listener.TopicListClickListener
import com.w36495.about.R
import com.w36495.about.data.Topic
import com.w36495.about.util.calDate

class TopicListAdapter : RecyclerView.Adapter<TopicListAdapter.TopicListViewHolder>() {

    private var topicList = arrayListOf<Topic>()
    private lateinit var topicListClickListener: TopicListClickListener

    class TopicListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val count: Button
        val topic: TextView
        val date: TextView
        val cardView: CardView

        init {
            count = view.findViewById(R.id.topic_list_item_think_count)
            topic = view.findViewById(R.id.topic_list_item_topic)
            date = view.findViewById(R.id.topic_list_item_date)
            cardView = view.findViewById(R.id.topic_list_item_cardView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicListViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_topic_list_item, parent, false)
        return TopicListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopicListViewHolder, position: Int) {
        holder.count.text = topicList[position].count.toString()
        holder.date.text = calDate(topicList[position].registDate).toString()
        holder.topic.text = topicList[position].topic
        holder.count.setBackgroundColor(Color.parseColor(topicList[position].color))

        holder.cardView.setOnClickListener {
            topicListClickListener.onTopicListItemClicked(topicList[position])
        }

        holder.cardView.setOnLongClickListener {
            Toast.makeText(it.context, "${topicList[position].topic} 삭제되었습니다.", Toast.LENGTH_SHORT)
                .show()
            deleteTopic(position)
            true
        }
    }

    override fun getItemCount(): Int = topicList.size

    fun setListClickListener(topicListClickListener: TopicListClickListener) {
        this.topicListClickListener = topicListClickListener
    }

    fun setTopicList(topicList: List<Topic>) {
        this.topicList = topicList as ArrayList<Topic>
        notifyDataSetChanged()
    }

    fun appTopicList(topic: Topic) {
        topicList.add(topic)
        notifyDataSetChanged()
    }

    fun deleteTopic(index: Int) {
        topicList.removeAt(index)
        notifyDataSetChanged()
    }
}