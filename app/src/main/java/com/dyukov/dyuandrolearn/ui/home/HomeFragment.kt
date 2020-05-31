package com.dyukov.dyuandrolearn.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.data.network.TaskModel
import com.dyukov.dyuandrolearn.data.network.UserModel
import com.dyukov.dyuandrolearn.databinding.FragmentHomeBinding
import com.dyukov.dyuandrolearn.extensions.toPx
import com.dyukov.dyuandrolearn.ui.MainActivity
import com.dyukov.dyuandrolearn.ui.home.adapter.HorizontalSpaceMarginItemDecoration
import com.dyukov.dyuandrolearn.ui.home.adapter.TaskListRvAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.tv_name
import kotlinx.android.synthetic.main.fragment_learn.*
import org.kodein.di.generic.instance

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding, HomeViewModelFactory>() {

    lateinit var taskListRvAdapter: TaskListRvAdapter

    override fun viewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun viewModelFactory(): HomeViewModelFactory = HomeViewModelFactory()

    private val mDatabaseReference: DatabaseReference? by instance<DatabaseReference>()

    override fun layoutResId(): Int = R.layout.fragment_home

    val userModel: UserModel by instance<UserModel>()

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setNavBarVisibility(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth.currentUser?.let {
            mDatabaseReference?.child(it.uid)?.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val user: UserModel? = p0.getValue(UserModel::class.java)
                    userModel.email = user?.email.orEmpty()
                    userModel.username = user?.username.orEmpty()
                    mDatabaseReference?.child(it.uid)?.removeEventListener(this);
                }
            })

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRvAdapter()
        tv_name.setText(userModel.username)

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