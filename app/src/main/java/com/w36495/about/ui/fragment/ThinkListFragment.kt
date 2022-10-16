package com.w36495.about.ui.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.w36495.about.ui.ItemSwipeHelper
import com.w36495.about.R
import com.w36495.about.ui.adapter.ThinkListAdapter
import com.w36495.about.domain.entity.Think
import com.w36495.about.contract.ThinkContract
import com.w36495.about.data.ThinkUiState
import com.w36495.about.ui.presenter.ThinkPresenter
import com.w36495.about.domain.entity.Topic
import com.w36495.about.data.local.AppDatabase
import com.w36495.about.data.repository.ThinkRepositoryImpl
import com.w36495.about.ui.dialog.ThinkDialogActivity
import com.w36495.about.ui.listener.ThinkListItemClickListener
import com.w36495.about.ui.listener.ThinkSwipeListener
import com.w36495.about.util.DateFormat
import kotlinx.coroutines.launch

class ThinkListFragment(private val topic: Topic) : Fragment(),
    ThinkSwipeListener, ThinkListItemClickListener, ThinkContract.View {

    private lateinit var thinkListContext: Context

    companion object {
        const val DIALOG_ADD_TAG: String = "THINK_ADD_TAG"
        const val DIALOG_UPDATE_TAG: String = "THINK_UPDATE_TAG"

        const val DIALOG_ADD_RESULT_CODE: Int = 999
        const val DIALOG_UPDATE_RESULT_CODE: Int = 888
    }

    private lateinit var getResultThink: ActivityResultLauncher<Intent>

    private lateinit var toolbar: MaterialToolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var thinkListAdapter: ThinkListAdapter
    private lateinit var itemSwipeHelper: ItemSwipeHelper

    private var database: AppDatabase? = null
    private lateinit var presenter: ThinkContract.Presenter

    private var position: Int? = null
    private var currentThink: Think? = null

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
        thinkListContext = view.context

        database = AppDatabase.getInstance(view.context)

        toolbar = view.findViewById(R.id.think_list_toolbar)
        recyclerView = view.findViewById(R.id.think_list_recyclerview)
        presenter = ThinkPresenter(ThinkRepositoryImpl(database!!.thinkDao()), this)

        toolbar.title = topic.topic

        thinkListAdapter = ThinkListAdapter()
        thinkListAdapter.setClickListener(this, this)

        itemSwipeHelper = ItemSwipeHelper(thinkListAdapter)
        val itemTouchHelper = ItemTouchHelper(itemSwipeHelper)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.adapter = thinkListAdapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        getResultThink = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == DIALOG_ADD_RESULT_CODE) {
                result.data?.let {
                    val think = Think(
                        topicId = topic.id,
                        text = it.getStringExtra("think")!!,
                        registDate = DateFormat.currentDateFormat(),
                        updateDate = DateFormat.currentDateFormat()
                    )
                    (presenter as ThinkPresenter).saveThink(think)
                }
            } else if (result.resultCode == DIALOG_UPDATE_RESULT_CODE) {
                result.data?.let { intent ->
                    currentThink?.let { think ->
                        think.text = intent.getStringExtra("think")!!
                        think.updateDate = DateFormat.currentDateFormat()
                    }
                    (presenter as ThinkPresenter).updateThink(currentThink!!)
                }
            } else {
                println("===== getResultThink - Failed =====")
            }
        }

        toolbar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.main_add -> {
                    val moveThinkAddIntent =
                        Intent(thinkListContext, ThinkDialogActivity::class.java)
                    moveThinkAddIntent.putExtra("tag", DIALOG_ADD_TAG)
                    getResultThink.launch(moveThinkAddIntent)
                    true
                }
                else -> false
            }
        }

        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        showThinks()
    }

    private fun showThinks() {
        presenter.getThinkList(topic.id)

        lifecycleScope.launch {
            (presenter as ThinkPresenter).uiState.collect { uiState ->
                when (uiState) {
                    is ThinkUiState.Loading -> {
                        println("===== loading =====")
                    }
                    is ThinkUiState.Empty -> {
                        println("===== empty =====")
                    }
                    is ThinkUiState.Success -> {
                        thinkListAdapter.setThinkList(uiState.thinkList)
                    }
                    is ThinkUiState.Failed -> {
                        println("===== failed : ${uiState.message} =====")
                    }
                }
            }
        }
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
        currentThink = think
        position?.let {
            val moveThinkUpdateIntent = Intent(thinkListContext, ThinkDialogActivity::class.java)
            moveThinkUpdateIntent.putExtra("tag", DIALOG_UPDATE_TAG)
            moveThinkUpdateIntent.putExtra("position", it)
            moveThinkUpdateIntent.putExtra("think", think.text)
            getResultThink.launch(moveThinkUpdateIntent)
        }
    }

    override fun showToast(message: String) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable {
            Toast.makeText(thinkListContext, message, Toast.LENGTH_SHORT).show()
        }, 0)
        showThinks()
    }

    override fun showError(message: String) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable {
            Toast.makeText(thinkListContext, message, Toast.LENGTH_SHORT).show()
        }, 0)
    }
}