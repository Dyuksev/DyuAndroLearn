package com.dyukov.dyuandrolearn.ui.taskDetail

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.databinding.FragmentTaskDetailBinding
import com.dyukov.dyuandrolearn.ui.MainActivity
import com.dyukov.dyuandrolearn.utils.Constants
import kotlinx.android.synthetic.main.fragment_task_detail.*

class TaskDetailFragment :
    BaseFragment<TaskDetailViewModel, FragmentTaskDetailBinding, TaskDetailViewModelFactory>() {

    override fun viewModelClass(): Class<TaskDetailViewModel> = TaskDetailViewModel::class.java

    override fun viewModelFactory(): TaskDetailViewModelFactory =
        TaskDetailViewModelFactory(lessonsRepository)

    override fun layoutResId(): Int = R.layout.fragment_task_detail

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setNavBarVisibility(false)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.taskId = requireArguments().getInt(Constants.KEY_ID_TASK)
            viewModel.isFromRecommend.value =
                requireArguments().getBoolean(Constants.FROM_RECOMMENDED)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTask()
        setData()
        initView()

    }

    fun initView() {
        iv_back.setOnClickListener {
            findNavController().popBackStack()
        }
        bt_done.setOnClickListener {
            viewModel.updateTask()
            (activity as MainActivity).showProgress()
            Handler().postDelayed({
                (activity as MainActivity).hideProgress()
                findNavController().popBackStack()
            }, 2000)

        }
    }

    fun setData() {
        viewModel.task.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                tv_main_text.text =
                    HtmlCompat.fromHtml(it.composition, HtmlCompat.FROM_HTML_MODE_LEGACY);
            }
        })
    }
}