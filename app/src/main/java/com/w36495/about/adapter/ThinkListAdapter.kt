package com.w36495.about.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.w36495.about.R
import com.w36495.about.data.Think
import com.w36495.about.listener.ItemSwipeListener
import com.w36495.about.listener.ThinkSwipeListener

class ThinkListAdapter : RecyclerView.Adapter<ThinkListAdapter.ThinkListViewHolder>(),
    ItemSwipeListener {

    private var thinkList = arrayListOf<Think>()
    private lateinit var thinkSwipeListener: ThinkSwipeListener

    class ThinkListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val number: TextView
        val text: TextView

        init {
            number = view.findViewById(R.id.think_list_item_number)
            text = view.findViewById(R.id.think_list_item_text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThinkListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_think_list_item, parent, false)
        return ThinkListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ThinkListViewHolder, position: Int) {
        holder.text.text = thinkList[position].text
        holder.number.text = (position + 1).toString()
    }

    override fun getItemCount(): Int = thinkList.size

    fun setClickListener(thinkSwipeListener: ThinkSwipeListener) {
        this.thinkSwipeListener = thinkSwipeListener
    }

    fun addThink(think: Think) {
        thinkList.add(think)
        notifyDataSetChanged()
    }

    override fun onItemSwiped(position: Int) {
        thinkList.removeAt(position)
        notifyDataSetChanged()
    }
}