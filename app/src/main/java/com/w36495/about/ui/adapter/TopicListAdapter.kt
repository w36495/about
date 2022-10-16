package com.w36495.about.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.w36495.about.ui.listener.TopicListClickListener
import com.w36495.about.R
import com.w36495.about.domain.entity.Topic
import com.w36495.about.util.DateFormat

class TopicListAdapter(
    private val context: Context,
    private val colors: List<String>
) : RecyclerView.Adapter<TopicListAdapter.TopicListViewHolder>() {

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
        if (position != 0) {
            val margin = dpToPx(16)
            val layoutParams: FrameLayout.LayoutParams =
                holder.cardView.layoutParams as FrameLayout.LayoutParams
            layoutParams.setMargins(margin, 0, margin, margin)
            holder.cardView.layoutParams = layoutParams
        }

        setCountBackgroundColor(holder, position)

        holder.count.text = topicList[position].count.toString()
        holder.date.text = DateFormat.calDate(topicList[position].registDate).toString()
        holder.topic.text = topicList[position].topic

        holder.cardView.setOnClickListener {
            topicListClickListener.onTopicListItemClicked(topicList[position].id)
        }

        holder.cardView.setOnLongClickListener {
            topicListClickListener.onTopicDeleteClicked(topicList[position].id, topicList[position])
            true
        }
    }

    override fun getItemCount(): Int = topicList.size

    fun setClickListener(topicListClickListener: TopicListClickListener) {
        this.topicListClickListener = topicListClickListener
    }

    fun setTopicList(topicList: List<Topic>) {
        this.topicList = topicList as ArrayList<Topic>
        notifyDataSetChanged()
    }

    private fun dpToPx(dp: Int): Int {
        val px: Float = dp * context.resources.displayMetrics.density
        return px.toInt()
    }

    private fun setCountBackgroundColor(holder: TopicListViewHolder, position: Int) {
        if (topicList[position].count <= 10) {
            holder.count.setBackgroundColor(Color.parseColor(colors[0]))
        } else if (topicList[position].count <= 30) {
            holder.count.setBackgroundColor(Color.parseColor(colors[1]))
        } else if (topicList[position].count <= 50) {
            holder.count.setBackgroundColor(Color.parseColor(colors[2]))
        } else {
            holder.count.setBackgroundColor(Color.parseColor(colors[3]))
        }
    }
}