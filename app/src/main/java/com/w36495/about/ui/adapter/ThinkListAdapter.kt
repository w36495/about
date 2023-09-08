package com.w36495.about.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.w36495.about.R
import com.w36495.about.domain.entity.Think
import com.w36495.about.ui.listener.ItemSwipeListener
import com.w36495.about.ui.listener.ThinkListItemClickListener
import com.w36495.about.ui.listener.ThinkSwipeListener

class ThinkListAdapter : RecyclerView.Adapter<ThinkListAdapter.ThinkListViewHolder>(),
    ItemSwipeListener {

    private var thinkList = arrayListOf<Think>()
    private lateinit var thinkSwipeListener: ThinkSwipeListener
    private lateinit var thinkListItemClickListener: ThinkListItemClickListener

    class ThinkListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val number: TextView
        val text: TextView
        val container: ConstraintLayout

        init {
            number = view.findViewById(R.id.think_list_item_number)
            text = view.findViewById(R.id.think_list_item_text)
            container = view.findViewById(R.id.think_list_item_container)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThinkListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_think_list, parent, false)
        return ThinkListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ThinkListViewHolder, position: Int) {
        holder.text.text = thinkList[position].text
        holder.number.text = (position + 1).toString()

        holder.container.setOnClickListener {
            thinkListItemClickListener.onThinkListItemClicked(position, thinkList[position])
        }
    }

    override fun getItemCount(): Int = thinkList.size

    fun setClickListener(thinkSwipeListener: ThinkSwipeListener, thinkListItemClickListener: ThinkListItemClickListener) {
        this.thinkSwipeListener = thinkSwipeListener
        this.thinkListItemClickListener = thinkListItemClickListener
    }

    fun addThink(think: Think) {
        thinkList.add(think)
        notifyDataSetChanged()
    }

    override fun onItemSwiped(position: Int) {
        thinkSwipeListener.onThinkSwiped(thinkList[position].id)
    }

    fun setThinkList(thinkList: List<Think>) {
        this.thinkList = thinkList as ArrayList<Think>
        notifyDataSetChanged()
    }
}