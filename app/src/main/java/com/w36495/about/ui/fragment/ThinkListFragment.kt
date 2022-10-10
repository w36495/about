package com.w36495.about.ui.fragment

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
import com.w36495.about.ui.ItemSwipeHelper
import com.w36495.about.R
import com.w36495.about.ui.adapter.ThinkListAdapter
import com.w36495.about.domain.entity.Think
import com.w36495.about.contract.ThinkContract
import com.w36495.about.ui.presenter.ThinkPresenter
import com.w36495.about.domain.entity.Topic
import com.w36495.about.data.local.AppDatabase
import com.w36495.about.data.repository.ThinkRepository
import com.w36495.about.ui.dialog.ThinkAddDialog
import com.w36495.about.ui.dialog.ThinkUpdateDialog
import com.w36495.about.ui.listener.ThinkDialogClickListener
import com.w36495.about.ui.listener.ThinkListItemClickListener
import com.w36495.about.ui.listener.ThinkSwipeListener

class ThinkListFragment(private val topic: Topic) : Fragment(), ThinkDialogClickListener,
    ThinkSwipeListener, ThinkListItemClickListener, ThinkContract.View {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var thinkListAdapter: ThinkListAdapter

    private lateinit var itemSwipeHelper: ItemSwipeHelper

    private var database: AppDatabase? = null
    private lateinit var presenter: ThinkContract.Presenter

    private var position: Int? = null

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

        database = AppDatabase.getInstance(view.context)

        toolbar = view.findViewById(R.id.think_list_toolbar)
        recyclerView = view.findViewById(R.id.think_list_recyclerview)
        presenter = ThinkPresenter(ThinkRepository(database!!.thinkDao()), this)

        toolbar.title = topic.topic

        thinkListAdapter = ThinkListAdapter()
        thinkListAdapter.setClickListener(this, this)

        itemSwipeHelper = ItemSwipeHelper(thinkListAdapter)
        val itemTouchHelper = ItemTouchHelper(itemSwipeHelper)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.adapter = thinkListAdapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        val windowManager = view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay

        display.getSize(size)

        toolbar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.main_add -> {
                    ThinkAddDialog(topic.id, size, this).show(
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

        presenter.getThinkList(topic.id)
    }

    override fun onThinkSaveClicked(think: Think) {
        presenter.saveThink(think)
    }

    override fun onThinkUpdateClicked(think: Think) {
        presenter.updateThink(think)
    }

    override fun onThinkSwiped(thinkId: Long) {
        presenter.deleteThinkById(thinkId)
    }

    override fun onThinkListItemClicked(position: Int, think: Think) {
        this.position = position
        showThink(think)
    }

    override fun showThinkList(thinkList: List<Think>) {
        thinkListAdapter.setThinkList(thinkList)
    }

    override fun showThink(think: Think) {
        position?.let {
            ThinkUpdateDialog(it, think, size, this).show(
                parentFragmentManager, "Think Item Update"
            )
        }
    }
}