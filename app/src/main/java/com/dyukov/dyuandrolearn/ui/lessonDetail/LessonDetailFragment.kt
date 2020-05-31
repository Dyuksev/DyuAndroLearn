package com.dyukov.dyuandrolearn.ui.lessonDetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.data.network.TaskModel
import com.dyukov.dyuandrolearn.databinding.FragmentLessonDetailBinding
import com.dyukov.dyuandrolearn.extensions.toPx
import com.dyukov.dyuandrolearn.ui.home.adapter.HorizontalSpaceMarginItemDecoration
import com.dyukov.dyuandrolearn.ui.home.adapter.TaskListRvAdapter
import com.dyukov.dyuandrolearn.ui.lessonDetail.adapter.TaskLessonRvAdapter
import kotlinx.android.synthetic.main.fragment_learn.*

class LessonDetailFragment :
    BaseFragment<LessonDetailViewModel, FragmentLessonDetailBinding, LessonDetailViewModelFactory>() {

    lateinit var taskListRvAdapter: TaskLessonRvAdapter

    override fun viewModelClass(): Class<LessonDetailViewModel> = LessonDetailViewModel::class.java

    override fun viewModelFactory(): LessonDetailViewModelFactory = LessonDetailViewModelFactory()

    override fun layoutResId(): Int = R.layout.fragment_lesson_detail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRvTasks()
        initTestData()
    }

    fun initRvTasks() {
        taskListRvAdapter = TaskLessonRvAdapter(requireContext())
        val gridLayoutManager = GridLayoutManager(context, 3)
        rv_tasks?.apply {
            layoutManager = gridLayoutManager
            adapter = taskListRvAdapter
        }
        taskListRvAdapter.setOnClickListener(object : TaskLessonRvAdapter.OnItemClicked {
            override fun onItemClick(position: Int) {
                findNavController().navigate(R.id.tast_detail_fragment)
            }
        })
    }

    fun initTestData() {
        val tast1 = TaskModel(1, 1, "Introduction", "Desc", points = 4, isCompleted = true)
        val tast2 = TaskModel(1, 1, "View", "Desc")
        val tast3 = TaskModel(1, 1, "More", "Desc")
        val tast4 = TaskModel(1, 1, "More", "Desc")
        taskListRvAdapter.setItems(listOf(tast1, tast2, tast3, tast4))
    }

}