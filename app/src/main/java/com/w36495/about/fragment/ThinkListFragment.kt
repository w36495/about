package com.w36495.about.fragment

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.w36495.about.ItemSwipeHelper
import com.w36495.about.R
import com.w36495.about.adapter.ThinkListAdapter
import com.w36495.about.data.Think
import com.w36495.about.data.Topic
import com.w36495.about.dialog.ThinkAddDialog
import com.w36495.about.listener.ThinkDialogClickListener
import com.w36495.about.listener.ThinkSwipeListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ThinkListFragment(private val topicId: Long) : Fragment(), ThinkDialogClickListener, ThinkSwipeListener {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var thinkListAdapter: ThinkListAdapter

    private lateinit var itemSwipeHelper: ItemSwipeHelper

    private val size = Point()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_think_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById(R.id.think_list_toolbar)
        recyclerView = view.findViewById(R.id.think_list_recyclerview)

        thinkListAdapter = ThinkListAdapter()
        thinkListAdapter.setClickListener(this)

        itemSwipeHelper = ItemSwipeHelper(thinkListAdapter)
        val itemTouchHelper = ItemTouchHelper(itemSwipeHelper)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.adapter = thinkListAdapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        val windowManager = view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay

        display.getSize(size)

        toolbar.title = topic.topic
        toolbar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.main_add -> {
                    ThinkAddDialog(size, this).show(
                        parentFragmentManager, "think"
                    )
                    true
                }
                else -> false
            }
        }

        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onThinkSaveClicked(think: Think) {
        thinkListAdapter.addThink(think)
    }
}