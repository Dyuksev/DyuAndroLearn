package com.dyukov.dyuandrolearn.ui.learn

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.base.BaseListAdapter
import com.dyukov.dyuandrolearn.data.db.model.Theory
import com.dyukov.dyuandrolearn.databinding.FragmentLoginBinding
import com.dyukov.dyuandrolearn.extensions.toPx
import com.dyukov.dyuandrolearn.ui.MainActivity
import com.dyukov.dyuandrolearn.ui.home.adapter.HorizontalSpaceMarginItemDecoration
import com.dyukov.dyuandrolearn.ui.learn.adapter.TheoryListAdapter
import com.dyukov.dyuandrolearn.utils.Constants
import kotlinx.android.synthetic.main.fragment_learn.*

class MainActivity : AppCompatActivity() {

}

class LearnFragment : BaseFragment<LearnViewModel, FragmentLoginBinding, LearnViewModelFactory>() {

    var theoryListAdapter = TheoryListAdapter()

    override fun viewModelClass(): Class<LearnViewModel> = LearnViewModel::class.java

    override fun viewModelFactory(): LearnViewModelFactory =
        LearnViewModelFactory(lessonsRepository)

    override fun layoutResId(): Int = R.layout.fragment_learn

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRvAdapter()
        theoryListAdapter.listener = object : BaseListAdapter.Listener<Theory> {
            override fun onItemClick(item: Theory) {
                val bundle = Bundle()
                bundle.putInt(Constants.KEY_ID_TASK, item.id!!)
                findNavController().navigate(R.id.tast_detail_fragment, bundle)
            }

        }
        viewModel.getTheories()
        viewModel.theories.observe(viewLifecycleOwner, Observer {
            if (it != null)
                theoryListAdapter.submitList(it)
        })

        iv_close?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setNavBarVisibility(false)
    }

    private fun initRvAdapter() {
        initRvTasks()
    }

    private fun initRvTasks() {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_theories?.apply {
            layoutManager = linearLayoutManager
            adapter = theoryListAdapter
        }
    }

}