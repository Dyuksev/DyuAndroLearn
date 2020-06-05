package com.dyukov.dyuandrolearn.ui.home

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.data.db.model.Task
import com.dyukov.dyuandrolearn.data.db.model.User
import com.dyukov.dyuandrolearn.databinding.FragmentHomeBinding
import com.dyukov.dyuandrolearn.extensions.toPx
import com.dyukov.dyuandrolearn.ui.MainActivity
import com.dyukov.dyuandrolearn.ui.home.adapter.HorizontalSpaceMarginItemDecoration
import com.dyukov.dyuandrolearn.ui.home.adapter.TaskListRvAdapter
import com.dyukov.dyuandrolearn.utils.Constants
import com.dyukov.dyuandrolearn.utils.PreferenceStorage
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.generic.instance


class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding, HomeViewModelFactory>() {

    lateinit var taskListRvAdapter: TaskListRvAdapter

    override fun viewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun viewModelFactory(): HomeViewModelFactory = HomeViewModelFactory(lessonsRepository)

    private val mDatabaseReference: DatabaseReference? by instance<DatabaseReference>()
    private val preferenceStorage: PreferenceStorage? by instance<PreferenceStorage>()

    override fun layoutResId(): Int = R.layout.fragment_home

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setNavBarVisibility(true)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRvAdapter()

        viewModel.getUserModel()
        viewModel.getSuggestedTasks()
        viewModel.taskFromDb.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                taskListRvAdapter.setItems(it)
                taskListRvAdapter.notifyDataSetChanged()
            }
        })
        viewModel.user.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setUserData(it)
            }
        })
    }


    fun setUserData(user: User) {
        tv_name.text = user.name
        viewModel.progresString.value =
            getString(R.string.points_count, user.progress.toString())
        viewModel.points.value = user.level.toString()
        ObjectAnimator.ofInt(level_bar, "progress", user.progress)
            .setDuration(300)
            .start()

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
            override fun onItemClick(position: Int, task: Task) {
                val bundle = Bundle()
                bundle.putInt(Constants.KEY_ID_TASK, task.id!!)
                bundle.putBoolean(Constants.FROM_RECOMMENDED, true)
                findNavController().navigate(R.id.tast_detail_fragment, bundle)
            }
        })
    }
}