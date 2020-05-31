package com.dyukov.dyuandrolearn.ui.learn

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.data.network.TaskModel
import com.dyukov.dyuandrolearn.data.network.UserModel
import com.dyukov.dyuandrolearn.data.network.LessonModel
import com.dyukov.dyuandrolearn.databinding.FragmentLoginBinding
import com.dyukov.dyuandrolearn.extensions.toPx
import com.dyukov.dyuandrolearn.ui.home.adapter.HorizontalSpaceMarginItemDecoration
import com.dyukov.dyuandrolearn.ui.home.adapter.TaskListRvAdapter
import com.dyukov.dyuandrolearn.ui.learn.adapter.LessonsListRvAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_learn.*
import org.kodein.di.generic.instance

class LearnFragment : BaseFragment<LearnViewModel, FragmentLoginBinding, LearnViewModelFactory>() {

    lateinit var taskListRvAdapter: TaskListRvAdapter
    lateinit var practiceTasksListRvAdapter: TaskListRvAdapter
    lateinit var lessonsListRvAdapter: LessonsListRvAdapter

    override fun viewModelClass(): Class<LearnViewModel> = LearnViewModel::class.java

    override fun viewModelFactory(): LearnViewModelFactory = LearnViewModelFactory()


    override fun layoutResId(): Int = R.layout.fragment_learn

    val userModel: UserModel by instance<UserModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRvAdapter()
        initTestData()
    }

    fun initTestData() {
        val tast1 = TaskModel(1, 1, "Introduction", "Desc")
        val tast2 = TaskModel(2, 1, "View", "Desc")
        val tast3 = TaskModel(4, 1, "More", "Desc")
        taskListRvAdapter.setItems(listOf(tast1, tast2, tast3))

        val lessonModel = LessonModel(1, "Introduction", "Introduction", 20, type = 1)
        val lessonModel2 = LessonModel(1, "View", "Introduction", 20, type = 2)

        lessonsListRvAdapter.setItems(listOf(lessonModel, lessonModel2))

        val tast4 = TaskModel(3, 1, "Introduction", "Desc")
        val tast5 = TaskModel(3, 1, "View", "Desc")
        val tast6 = TaskModel(4, 1, "More", "Desc")
        practiceTasksListRvAdapter.setItems(listOf(tast4, tast5, tast6))
    }

    fun initRvAdapter() {
        initRvTasks()
        initRvLessons()
        initRvPractice()
    }

    fun initRvTasks() {
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
            override fun onItemClick(position: Int) {
                Toast.makeText(requireContext(), "LOL", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun initRvLessons() {
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
            override fun onItemClick(position: Int) {
                Toast.makeText(requireContext(), "LOL", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun initRvPractice() {
        practiceTasksListRvAdapter = TaskListRvAdapter(requireContext())
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val decorator = HorizontalSpaceMarginItemDecoration(0f.toPx(), 0f.toPx(), 8f.toPx())
        rv_practice?.apply {
            addItemDecoration(decorator)
            layoutManager = linearLayoutManager
            adapter = practiceTasksListRvAdapter
        }
        lessonsListRvAdapter.setOnClickListener(object : LessonsListRvAdapter.OnItemClicked {
            override fun onItemClick(position: Int) {
                Toast.makeText(requireContext(), "LOL", Toast.LENGTH_LONG).show()
            }
        })
    }
}