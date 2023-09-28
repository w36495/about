package com.w36495.about.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.w36495.about.databinding.ItemThinkListBinding
import com.w36495.about.domain.entity.Think
import com.w36495.about.ui.listener.ThinkListItemListener

class ThinkListAdapter : RecyclerView.Adapter<ThinkListViewHolder>() {

    private var thinkList = arrayListOf<Think>()
    private lateinit var thinkListItemListener: ThinkListItemListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThinkListViewHolder {
        return ThinkListViewHolder(ItemThinkListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ThinkListViewHolder, position: Int) {
        holder.bind(thinkList[position])

        holder.onSwipeItem = {
            thinkListItemListener.onSwipeItem(thinkList[position].id)
        }

        holder.onClickItem = {
            thinkListItemListener.onClickItem(position, thinkList[position])
        }
    }

    override fun getItemCount(): Int = thinkList.size

    fun setClickListener(thinkSwipeListener: ThinkListItemListener) {
        this.thinkListItemListener = thinkSwipeListener
    }

    fun setThinkList(thinkList: List<Think>) {
        this.thinkList = thinkList as ArrayList<Think>
        notifyDataSetChanged()
    }
}