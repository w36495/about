package com.w36495.about.ui.fragment

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.w36495.about.R
import com.w36495.about.ui.adapter.TopicListAdapter
import com.w36495.about.domain.entity.Topic
import com.w36495.about.contract.TopicContract
import com.w36495.about.ui.presenter.TopicPresenter
import com.w36495.about.data.local.AppDatabase
import com.w36495.about.data.repository.ThinkRepository
import com.w36495.about.data.repository.TopicRepository
import com.w36495.about.ui.dialog.TopicAddDialog
import com.w36495.about.ui.listener.TopicDialogClickListener
import com.w36495.about.ui.listener.TopicListClickListener

class TopicListFragment : Fragment(), TopicListClickListener, TopicDialogClickListener, TopicContract.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var topicListAdapter: TopicListAdapter
    private lateinit var toolbar: MaterialToolbar

    private var database: AppDatabase? = null
    private lateinit var presenter: TopicContract.Presenter

    private val size = Point()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_topic_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _colors = resources.getStringArray(R.array.about_default_gradient_grey)
        val colors = arrayListOf<String>()
        _colors.forEach { colors.add(it) }

        val windowManager = view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        display.getSize(size)

        database = AppDatabase.getInstance(view.context)

        recyclerView = view.findViewById(R.id.topic_list_recyclerview)
        toolbar = view.findViewById(R.id.topic_list_toolbar)

        topicListAdapter = TopicListAdapter(view.context, colors)
        topicListAdapter.setClickListener(this)

        recyclerView.adapter = topicListAdapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        toolbar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.main_add -> {
                    TopicAddDialog(size, this).show(
                        parentFragmentManager, "topic"
                    )
                    true
                }
                else -> false
            }
        }
        presenter = TopicPresenter(
            TopicRepository(database!!.topicDao()),
            ThinkRepository(database!!.thinkDao()),
            this)
        presenter.getTopicList()
    }

    override fun onTopicListItemClicked(topicId: Long) {
        presenter.getTopic(topicId)
    }

    override fun onTopicSaveClicked(topic: Topic): Boolean {
        if (!presenter.checkLengthOfTopic(topic.topic.length)) return false
        presenter.saveTopic(topic)
        return true
    }

    override fun onErrorTopicSaved() {
        // TODO : Topic 길이 제한이 1~10이 아닌 경우 -> 에러메세지 띄우기
    }

    override fun onTopicDeleteClicked(topicId: Long) {
        presenter.deleteTopicById(topicId)
        // TODO: Think 에서도 삭제되어야 함
    }

    override fun showTopicList(topicList: List<Topic>) {
        topicListAdapter.setTopicList(topicList)
    }

    override fun setTopic(topic: Topic) {
        parentFragmentManager.commit {
            replace(R.id.main_fragment_container, ThinkListFragment(topic))
            setReorderingAllowed(true)
            addToBackStack("thinkFragment")
        }
    }
}