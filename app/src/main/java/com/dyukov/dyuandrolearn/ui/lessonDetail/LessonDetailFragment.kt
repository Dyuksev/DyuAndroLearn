package com.dyukov.dyuandrolearn.ui.lessonDetail

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.data.db.model.Task
import com.dyukov.dyuandrolearn.data.db.model.User
import com.dyukov.dyuandrolearn.data.network.TaskModel
import com.dyukov.dyuandrolearn.databinding.FragmentLessonDetailBinding
import com.dyukov.dyuandrolearn.ui.lessonDetail.adapter.TaskLessonRvAdapter
import com.dyukov.dyuandrolearn.utils.Constants
import kotlinx.android.synthetic.main.fragment_learn.*
import kotlinx.android.synthetic.main.fragment_learn.rv_tasks
import kotlinx.android.synthetic.main.fragment_lesson_detail.*
import timber.log.Timber

class LessonDetailFragment :
    BaseFragment<LessonDetailViewModel, FragmentLessonDetailBinding, LessonDetailViewModelFactory>() {

    lateinit var taskListRvAdapter: TaskLessonRvAdapter

    override fun viewModelClass(): Class<LessonDetailViewModel> = LessonDetailViewModel::class.java

    override fun viewModelFactory(): LessonDetailViewModelFactory =
        LessonDetailViewModelFactory(lessonsRepository)

    override fun layoutResId(): Int = R.layout.fragment_lesson_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.lessonId = requireArguments().getInt(Constants.KEY_ID_LESSON)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRvTasks()
        initView()
        viewModel.getUserModel()
        viewModel.getTasksByLesson()
        viewModel.taskFromDb.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                taskListRvAdapter.setItems(it)
                viewModel.countCompletedTasks(requireContext())
                ObjectAnimator.ofInt(pb_level_progress, "progress", viewModel.progress.value ?: 0)
                    .setDuration(300)
                    .start()

            }
        })
        viewModel.isLessonUpdated.observe(viewLifecycleOwner, Observer {
            if (it != null && it == true && viewModel.progress.value != 100) {
                findNavController().popBackStack()
            }
        })

    }

    private fun initView() {
        iv_back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initRvTasks() {
        taskListRvAdapter = TaskLessonRvAdapter(requireContext())
        val gridLayoutManager = GridLayoutManager(context, 3)
        rv_tasks?.apply {
            layoutManager = gridLayoutManager
            adapter = taskListRvAdapter
        }
        taskListRvAdapter.setOnClickListener(object : TaskLessonRvAdapter.OnItemClicked {
            override fun onItemClick(position: Int, task: Task) {
                val bundle = Bundle()
                bundle.putInt(Constants.KEY_ID_TASK, task.id!!)
                bundle.putBoolean(Constants.FROM_RECOMMENDED, false)
                findNavController().navigate(R.id.tast_detail_fragment, bundle)
            }
        })
    }

}