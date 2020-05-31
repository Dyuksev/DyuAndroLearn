package com.dyukov.dyuandrolearn.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.data.db.network.TaskModel
import com.dyukov.dyuandrolearn.databinding.FragmentHomeBinding
import com.dyukov.dyuandrolearn.extensions.toPx
import com.dyukov.dyuandrolearn.ui.MainActivity
import com.dyukov.dyuandrolearn.ui.home.adapter.HorizontalSpaceMarginItemDecoration
import com.dyukov.dyuandrolearn.ui.home.adapter.TaskListRvAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding, HomeViewModelFactory>() {

    lateinit var taskListRvAdapter: TaskListRvAdapter

    override fun viewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun viewModelFactory(): HomeViewModelFactory = HomeViewModelFactory()

    override fun layoutResId(): Int = R.layout.fragment_home

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setNavBarVisibility(true)


    }

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
    }

    fun initRvAdapter() {
        taskListRvAdapter = TaskListRvAdapter(requireContext())
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        val decorator = HorizontalSpaceMarginItemDecoration(0f.toPx(), 0f.toPx(), 8f.toPx())
        rv_recommendations?.apply {
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
}