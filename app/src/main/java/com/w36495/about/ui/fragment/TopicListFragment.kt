package com.w36495.about.ui.fragment

import android.content.Context
import android.content.Intent
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
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.w36495.about.R
import com.w36495.about.ui.adapter.TopicListAdapter
import com.w36495.about.domain.entity.Topic
import com.w36495.about.contract.TopicContract
import com.w36495.about.data.TopicUiState
import com.w36495.about.ui.presenter.TopicPresenter
import com.w36495.about.data.local.AppDatabase
import com.w36495.about.data.repository.ThinkRepositoryImpl
import com.w36495.about.data.repository.TopicRepositoryImpl
import com.w36495.about.ui.dialog.TopicDialogActivity
import com.w36495.about.ui.listener.ResetClickListener
import com.w36495.about.ui.listener.TopicListClickListener
import kotlinx.coroutines.launch

class TopicListFragment : Fragment(), TopicListClickListener, TopicContract.View, ResetClickListener {

    private lateinit var topicListContext: Context
    private var database: AppDatabase? = null

    private val THINK_LIST_TAG: String = "THINK_LIST_FRAGMENT"

    companion object {
        val INTENT_RESULT_TOPIC: Int = 100
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var topicListAdapter: TopicListAdapter
    private lateinit var toolbar: MaterialToolbar
    private lateinit var presenter: TopicContract.Presenter
    private lateinit var getResultTopic: ActivityResultLauncher<Intent>

    private var handler: Handler? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_topic_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    private fun init(view: View) {
        topicListContext = view.context

        getResultTopic = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == INTENT_RESULT_TOPIC) {
                result.data?.let {
                    val topic = Topic(
                        it.getStringExtra("topic")!!,
                        it.getStringExtra("color")!!,
                        it.getStringExtra("date")!!
                    )
                    (presenter as TopicPresenter).saveTopic(topic)
                }
            } else {
                println("===== getResultTopic - Failed =====")
            }
        }

        val _colors = resources.getStringArray(R.array.about_default_gradient_grey)
        val colors = arrayListOf<String>()
        _colors.forEach { colors.add(it) }

        database = AppDatabase.getInstance(view.context)

        recyclerView = view.findViewById(R.id.topic_list_recyclerview)
        toolbar = view.findViewById(R.id.topic_list_toolbar)

        topicListAdapter = TopicListAdapter(topicListContext, colors)
        topicListAdapter.setClickListener(this)

        recyclerView.adapter = topicListAdapter
        recyclerView.layoutManager = LinearLayoutManager(topicListContext)

        presenter = TopicPresenter(
            TopicRepositoryImpl(database!!.topicDao()),
            ThinkRepositoryImpl(database!!.thinkDao()),
            this
        )

        toolbar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.main_add -> {
                    val moveTopicDialogIntent =
                        Intent(view.context, TopicDialogActivity::class.java)
                    getResultTopic.launch(moveTopicDialogIntent)
                    true
                }
                else -> false
            }
        }

        showTopics()
    }

    private fun showTopics() {
        presenter.getTopicList()

        lifecycleScope.launch {
            (presenter as TopicPresenter).uiState.collect { uiState ->
                when (uiState) {
                    is TopicUiState.Loading -> {
                        println("===== loading =====")
                    }
                    is TopicUiState.Empty -> {
                        println("===== empty =====")
                        topicListAdapter.setTopicEmptyList()
                    }
                    is TopicUiState.Success -> {
                        topicListAdapter.setTopicList(uiState.list)
                    }
                    is TopicUiState.Failed -> {
                        println("===== failed : ${uiState.message} =====")
                    }
                }
            }
        }
    }

    override fun onTopicListItemClicked(topicId: Long) {
        presenter.getTopic(topicId)
    }

    override fun onTopicDeleteClicked(topicId: Long, topic: Topic) {
        MaterialAlertDialogBuilder(topicListContext, R.style.alert_dialog_delete_style)
            .setTitle("주제 삭제")
            .setMessage("${topic.topic}에 등록된 ${topic.count}개의 생각들도 함께 삭제되며 복구가 불가능합니다.\n정말 삭제하시겠습니까?")
            .setNeutralButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("삭제") { _, _ ->
                presenter.deleteThinkListByTopicId(topicId)
                presenter.deleteTopicById(topicId)
            }
            .show()
    }

    override fun showTopicList(topicList: List<Topic>) {
        topicListAdapter.setTopicList(topicList)
    }

    /**
     * 하나의 주제 보이기
     */
    override fun showTopic(topic: Topic) {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(THINK_LIST_TAG)
            replace(R.id.main_fragment_container, ThinkListFragment(topic))
        }
    }

    /**
     * 비어있는 화면 보이기
     */
    override fun showEmptyTopicList() {
        showToast("초기화되었습니다.")
        showTopics()
    }

    override fun showError(tag: String, message: String?) {
        if (handler == null) {
            handler = Handler(Looper.getMainLooper())
        }

        handler?.let {
            if (tag == "TOPIC_DELETE") {
                it.postDelayed(Runnable { printToast(message = "삭제하는 과정에서 오류가 발생하였습니다.") }, 0)
            } else if (tag == "TOPIC_INSERT") {
                it.postDelayed(Runnable { printToast(message = "저장하는 과정에서 오류가 발생하였습니다.") }, 0)
            } else if (tag == "THINK_LIST_SIZE") {
                it.postDelayed(Runnable { printToast(message = "목록을 불러오는 과정에서 오류가 발생하였습니다") }, 0)
            } else if (tag == "THINK_ALL_DELETE" || tag == "TOPIC_ALL_DELETE") {
                it.postDelayed(Runnable { printToast(message = "초기화 과정에서 오류가 발생하였습니다") }, 0)
            } else if (tag == R.string.tag_topic_list_empty.toString()) {
                it.postDelayed(Runnable { printToast(message = message) }, 0)
            } else {

            }
        }

        handler = null
    }

    override fun showToast(message: String?) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable {
            Toast.makeText(topicListContext, message, Toast.LENGTH_SHORT).show()
        }, 0)
        showTopics()
    }


    private fun printToast(message: String?) {
        Toast.makeText(topicListContext, message, Toast.LENGTH_SHORT)
            .show()
    }

    override fun onResetClicked() {
        presenter.deleteAllTopic()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        database = null
        handler = null
    }
}