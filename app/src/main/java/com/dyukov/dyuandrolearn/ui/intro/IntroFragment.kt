package com.dyukov.dyuandrolearn.ui.intro

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.Px
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.data.network.LessonModel
import com.dyukov.dyuandrolearn.data.network.TaskModel
import com.dyukov.dyuandrolearn.data.network.UserModel
import com.dyukov.dyuandrolearn.databinding.FragmentIntroBinding
import com.dyukov.dyuandrolearn.ui.MainActivity
import com.dyukov.dyuandrolearn.ui.intro.adapter.FragmentsPagerAdapter
import com.dyukov.dyuandrolearn.ui.intro.stepOne.StepOneFragment
import com.dyukov.dyuandrolearn.ui.intro.stepThree.StepThreeFragment
import com.dyukov.dyuandrolearn.ui.intro.stepTwo.StepTwoFragment
import com.dyukov.dyuandrolearn.utils.PreferenceStorage
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_intro.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance

class IntroFragment : BaseFragment<IntroViewModel, FragmentIntroBinding, IntroViewModelFactory>() {

    var adapter: FragmentsPagerAdapter? = null

    override fun viewModelClass(): Class<IntroViewModel> = IntroViewModel::class.java

    override fun viewModelFactory(): IntroViewModelFactory =
        IntroViewModelFactory(lessonsRepository)

    private val mDatabaseReference: DatabaseReference? by instance<DatabaseReference>()

    private val preferenceStorage: PreferenceStorage? by instance<PreferenceStorage>()
    override fun layoutResId(): Int = R.layout.fragment_intro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getLessons()
        getUserData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPager()
        initViews()

    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setNavBarVisibility(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).setNavBarVisibility(true)
    }

    private fun initViews() {
        bt_next.setOnClickListener {
            viewModel.currentPosition.apply {
                if (preferenceStorage?.getDataLoaded() == true) {
                    if (viewModel.currentPosition == 2)
                        findNavController().navigate(R.id.action_intro_to_home)
                    else
                        vp_intro?.currentItem = viewModel.currentPosition + 1
                } else {
                    if (viewModel.currentPosition == 2)
                        Toast.makeText(context, getString(R.string.wait_toast), Toast.LENGTH_LONG)
                            .show()
                    else
                        vp_intro?.currentItem = viewModel.currentPosition + 1
                }
            }
        }
    }

    private fun setPager() {
        if (adapter == null) {
            adapter = FragmentsPagerAdapter(childFragmentManager)
            adapter?.apply {
                addFragment(StepOneFragment.newInstance())
                addFragment(StepTwoFragment.newInstance())
                addFragment(StepThreeFragment.newInstance())
            }
        }
        vp_intro.apply {
            adapter = this@IntroFragment.adapter
            tab_layout?.setupWithViewPager(this, true)
            tab_layout?.getTabAt(0)?.select()
            offscreenPageLimit = 1
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    @Px positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    viewModel.currentPosition = position
                    if (position == 2)
                        bt_next.text = "Go"
                    else bt_next.text = "Next"
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
        }

    }

    fun getUserData() {
        GlobalScope.launch(Dispatchers.Main) {
            mAuth.currentUser?.let {
                mDatabaseReference?.child(it.uid)
                    ?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            val user: UserModel? = p0.getValue(UserModel::class.java)

                            user?.let { userModel ->
                                viewModel.convertToDbUserAndInsert(userModel)
                            }
                        }
                    })
            }
        }
    }

    private fun getLessons() {
        if (preferenceStorage?.getDataLoaded() == false) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("lessons")
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val lessons = ArrayList<LessonModel>()
                    val tasks = ArrayList<TaskModel>()
                    for (lessonSnapshot in dataSnapshot.children) {
                        val lessonModel =
                            lessonSnapshot.getValue(LessonModel::class.java)
                        lessonModel?.let {
                            lessons.add(lessonModel)
                            lessonModel.tasks?.let { list -> tasks.addAll(list) }
                        }
                        preferenceStorage?.setIsDataLoaded(true)
                    }
                    viewModel.toDbLessons(lessons, tasks)
                }
            })

        }
    }

}