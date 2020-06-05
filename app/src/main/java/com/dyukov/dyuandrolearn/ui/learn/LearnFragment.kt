package com.dyukov.dyuandrolearn.ui.learn

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.data.db.model.Lesson
import com.dyukov.dyuandrolearn.data.db.model.Task
import com.dyukov.dyuandrolearn.databinding.FragmentLoginBinding
import com.dyukov.dyuandrolearn.extensions.toPx
import com.dyukov.dyuandrolearn.ui.MainActivity
import com.dyukov.dyuandrolearn.ui.home.adapter.HorizontalSpaceMarginItemDecoration
import com.dyukov.dyuandrolearn.ui.home.adapter.TaskListRvAdapter
import com.dyukov.dyuandrolearn.ui.learn.adapter.LessonsListRvAdapter
import com.dyukov.dyuandrolearn.utils.Constants
import kotlinx.android.synthetic.main.fragment_learn.*
import kotlinx.android.synthetic.main.fragment_learn.rv_tasks

class LearnFragment : BaseFragment<LearnViewModel, FragmentLoginBinding, LearnViewModelFactory>() {

    lateinit var taskListRvAdapter: TaskListRvAdapter
    lateinit var practiceTasksListRvAdapter: TaskListRvAdapter
    lateinit var lessonsListRvAdapter: LessonsListRvAdapter

    override fun viewModelClass(): Class<LearnViewModel> = LearnViewModel::class.java

    override fun viewModelFactory(): LearnViewModelFactory =
        LearnViewModelFactory(lessonsRepository)

    override fun layoutResId(): Int = R.layout.fragment_learn

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRvAdapter()
        viewModel.getUserModel()
        viewModel.getLessons()
        viewModel.lessonsFromDb.observe(viewLifecycleOwner, Observer {
            if (it != null)
                lessonsListRvAdapter.setItems(it)
        })
        viewModel.taskFromDb.observe(viewLifecycleOwner, Observer {
            if (it != null)
                taskListRvAdapter.setItems(it)
        })
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setNavBarVisibility(true)

    }

    private fun initRvAdapter() {
        initRvTasks()
        initRvLessons()
    }

    private fun initRvTasks() {
        taskListRvAdapter = TaskListRvAdapter(requireContext())
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val decorator = HorizontalSpaceMarginItemDecoration(0f.toPx(), 0f.toPx(), 8f.toPx())
        rv_tasks?.apply {
            addItemDecoration(decorator)
            layoutManager = linearLayoutManager
            adapter = taskListRvAdapter
        }
        taskListRvAdapter.setOnClickListener(object : TaskListRvAdapter.OnItemClicked {
            override fun onItemClick(position: Int, task: Task) {
                val bundle = Bundle()
                bundle.putInt(Constants.KEY_ID_TASK, task.id!!)
                bundle.putBoolean(Constants.FROM_RECOMMENDED, true)
                findNavController().navigate(R.id.tast_detail_fragment, bundle)
            }
        })
    }

    private fun initRvLessons() {
        lessonsListRvAdapter = LessonsListRvAdapter(requireContext())
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val decorator = HorizontalSpaceMarginItemDecoration(0f.toPx(), 0f.toPx(), 8f.toPx())
        rv_lessons?.apply {
            addItemDecoration(decorator)
            layoutManager = linearLayoutManager
            adapter = lessonsListRvAdapter
        }
        lessonsListRvAdapter.setOnClickListener(object : LessonsListRvAdapter.OnItemClicked {
            override fun onItemClick(position: Int, lesson: Lesson) {
                val bundle = Bundle()
                bundle.putInt(Constants.KEY_ID_LESSON, lesson.id!!)
                findNavController().navigate(R.id.detail_lesson_fragment, bundle)
            }
        })
    }

}