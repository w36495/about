package com.w36495.about.ui.fragment

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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.w36495.about.R
import com.w36495.about.ui.adapter.TopicListAdapter
import com.w36495.about.domain.entity.Topic
import com.w36495.about.contract.TopicListContract
import com.w36495.about.data.TopicListUiState
import com.w36495.about.ui.presenter.TopicPresenter
import com.w36495.about.data.local.AppDatabase
import com.w36495.about.data.repository.ThinkRepositoryImpl
import com.w36495.about.data.repository.TopicRepositoryImpl
import com.w36495.about.databinding.FragmentTopicListBinding
import com.w36495.about.domain.dto.TopicListDTO
import com.w36495.about.ui.dialog.TopicDialogActivity
import com.w36495.about.ui.listener.TopicListClickListener
import com.w36495.about.util.DateFormat
import kotlinx.coroutines.launch

class TopicListFragment : Fragment(), TopicListClickListener, TopicListContract.View {

    private var _binding: FragmentTopicListBinding? = null
    private val binding:FragmentTopicListBinding get() = _binding!!
    private var database: AppDatabase? = null

    companion object {
        const val INTENT_RESULT_TOPIC_INSERT: Int = 100
        const val INTENT_RESULT_TOPIC_UPDATE = 101

        const val TAG_THINK_LIST: String = "THINK_LIST_FRAGMENT"
        const val TAG_TOPIC_UPDATE = "TAG_TOPIC_UPDATE"
    }

    private lateinit var topicListAdapter: TopicListAdapter
    private lateinit var presenter: TopicListContract.Presenter
    private lateinit var getResultTopic: ActivityResultLauncher<Intent>

    private var handler: Handler? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTopicListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    private fun init(view: View) {

        getResultTopic = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == INTENT_RESULT_TOPIC_INSERT) {
                result.data?.let {
                    val topic = Topic(
                        topic = it.getCharSequenceExtra("topic").toString(),
                        registDate = DateFormat.currentDateFormat(),
                        updateDate = DateFormat.currentDateFormat()
                    )
                    (presenter as TopicPresenter).saveTopic(topic)
                }
            } else if (result.resultCode == INTENT_RESULT_TOPIC_UPDATE){
                result.data?.let {
                    val topicId = it.getLongExtra("topicId", -1)
                    val topic = it.getCharSequenceExtra("topic").toString()

                    presenter.updateTopic(topicId, topic, DateFormat.currentDateFormat())
                }
            }
        }

        val _colors = resources.getStringArray(R.array.about_default_gradient_grey)
        val colors = arrayListOf<String>()
        _colors.forEach { colors.add(it) }

        database = AppDatabase.getInstance(view.context)

        topicListAdapter = TopicListAdapter()
        topicListAdapter.setClickListener(this)

        binding.topicListRecyclerview.apply {
            adapter = topicListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        presenter = TopicPresenter(
            TopicRepositoryImpl(database!!.topicDao()),
            ThinkRepositoryImpl(database!!.thinkDao()),
            this
        )

        binding.topicListToolbar.setOnMenuItemClickListener { menu ->
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
            (presenter as TopicPresenter).topicListUiState.collect { uiState ->
                when (uiState) {
                    is TopicListUiState.Loading -> {
                        // TODO
                    }
                    is TopicListUiState.Empty -> {
                        topicListAdapter.setTopicEmptyList()
                    }
                    is TopicListUiState.Success -> {
                        topicListAdapter.setTopicList(uiState.topicList)
                    }
                    is TopicListUiState.Failed -> {
                        // TODO
                    }
                }
            }
        }
    }

    override fun onClickItem(topic: TopicListDTO) {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(TAG_THINK_LIST)
            replace(R.id.main_fragment_container, ThinkListFragment(topic))
        }
    }

    override fun onLongClickItem(topic: TopicListDTO) {
        val menu = arrayOf(resources.getString(R.string.dialog_menu_topic_update), resources.getString(R.string.dialog_menu_topic_delete))
        MaterialAlertDialogBuilder(requireContext())
            .setItems (menu) { dialog, which ->
                when(which) {
                    0 -> {
                        val moveTopicDialogIntent =
                            Intent(requireContext(), TopicDialogActivity::class.java)
                        moveTopicDialogIntent.putExtra("tag", TAG_TOPIC_UPDATE)
                        moveTopicDialogIntent.putExtra("topicId", topic.id)
                        moveTopicDialogIntent.putExtra("topic", topic.topic)
                        getResultTopic.launch(moveTopicDialogIntent)
                    }
                    1 -> {
                        MaterialAlertDialogBuilder(requireContext(), R.style.alert_dialog_delete_style)
                            .setTitle(resources.getString(R.string.dialog_title_delete))
                            .setMessage("${topic.topic}에 등록된 ${topic.countOfThink}개의 생각들도 함께 삭제되며 복구가 불가능합니다.\n정말 삭제하시겠습니까?")
                            .setNeutralButton(resources.getString(R.string.dialog_btn_cancel)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .setPositiveButton(resources.getString(R.string.dialog_btn_delete)) { _, _ ->
                                presenter.deleteTopicById(topic.id)
                            }
                            .show()
                    }
                }
            }.show()
    }

    override fun showError(tag: String, message: String?) {

    }

    override fun showToast(message: String?) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }, 0)
        showTopics()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        database = null
        handler = null
    }
}