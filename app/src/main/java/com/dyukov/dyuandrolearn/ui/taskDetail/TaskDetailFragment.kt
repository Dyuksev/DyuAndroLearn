package com.dyukov.dyuandrolearn.ui.taskDetail

import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.databinding.FragmentTaskDetailBinding

class TaskDetailFragment : BaseFragment<TaskDetailViewModel, FragmentTaskDetailBinding, TaskDetailViewModelFactory> () {

    override fun viewModelClass(): Class<TaskDetailViewModel> = TaskDetailViewModel::class.java

    override fun viewModelFactory(): TaskDetailViewModelFactory = TaskDetailViewModelFactory()

    override fun layoutResId(): Int = R.layout.fragment_task_detail
}