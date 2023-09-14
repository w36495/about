package com.w36495.about.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.w36495.about.databinding.ItemThinkListBinding
import com.w36495.about.domain.entity.Think
import com.w36495.about.ui.listener.ThinkListItemClickListener
import com.w36495.about.ui.listener.ThinkSwipeListener

class ThinkListAdapter : RecyclerView.Adapter<ThinkListViewHolder>() {

    private var thinkList = arrayListOf<Think>()
    private lateinit var thinkSwipeListener: ThinkSwipeListener
    private lateinit var thinkListItemClickListener: ThinkListItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThinkListViewHolder {
        return ThinkListViewHolder(ItemThinkListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ThinkListViewHolder, position: Int) {
        holder.bind(thinkList[position])

        holder.onSwipeItem = {
            thinkSwipeListener.onThinkSwiped(thinkList[position].id)
        }
    }

    override fun getItemCount(): Int = thinkList.size

    fun setClickListener(
        thinkSwipeListener: ThinkSwipeListener,
        thinkListItemClickListener: ThinkListItemClickListener
    ) {
        this.thinkSwipeListener = thinkSwipeListener
        this.thinkListItemClickListener = thinkListItemClickListener
    }

    fun addThink(think: Think) {
        thinkList.add(think)
        notifyDataSetChanged()
    }

    fun setThinkList(thinkList: List<Think>) {
        this.thinkList = thinkList as ArrayList<Think>
        notifyDataSetChanged()
    }
}