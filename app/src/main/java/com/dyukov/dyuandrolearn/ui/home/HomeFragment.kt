package com.dyukov.dyuandrolearn.ui.home

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.base.BaseListAdapter
import com.dyukov.dyuandrolearn.data.db.model.Lesson
import com.dyukov.dyuandrolearn.data.db.model.User
import com.dyukov.dyuandrolearn.data.network.DyuData
import com.dyukov.dyuandrolearn.data.network.UserModel
import com.dyukov.dyuandrolearn.databinding.DialogLessonDoneBinding
import com.dyukov.dyuandrolearn.databinding.DialogLessonErrorBinding
import com.dyukov.dyuandrolearn.databinding.DialogLevelUpBinding
import com.dyukov.dyuandrolearn.databinding.FragmentHomeBinding
import com.dyukov.dyuandrolearn.extensions.fadeIn
import com.dyukov.dyuandrolearn.extensions.fadeOut
import com.dyukov.dyuandrolearn.extensions.toPx
import com.dyukov.dyuandrolearn.global.EventObserver
import com.dyukov.dyuandrolearn.global.GlobalEventController
import com.dyukov.dyuandrolearn.ui.MainActivity
import com.dyukov.dyuandrolearn.ui.home.adapter.LessonsListAdapter
import com.dyukov.dyuandrolearn.utils.Constants
import com.dyukov.dyuandrolearn.utils.PreferenceStorage
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_task_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance


class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding, HomeViewModelFactory>() {

    var navItem: Lesson? = null
    var lessonsListAdapter = LessonsListAdapter()

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
        lessonsListAdapter.listener = object : BaseListAdapter.Listener<Lesson> {
            override fun onItemClick(item: Lesson) {
                handleLessonClick(item)
            }
        }
        getData()

    }

    fun handleLessonClick(lesson: Lesson) {
        when (lesson.type) {
            LessonsListAdapter.TYPE_AVAILABLE -> {
                navItem = lesson
                viewModel.getTasksByLesson(lesson)
            }
            LessonsListAdapter.TYPE_PROGRESS -> {
                navItem = lesson
                viewModel.getTasksByLesson(lesson)
            }
            LessonsListAdapter.TYPE_DONE -> {
                showDoneLessonDialog(lesson)
            }
            LessonsListAdapter.TYPE_UNAVAILABLE -> {
                showUnAvailableLessonDialog(lesson)
            }
        }
    }

    private fun showUnAvailableLessonDialog(lesson: Lesson) {
        val dialogBinding: DialogLessonErrorBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_lesson_error,
                null,
                false
            )
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        dialogBinding.tvStart.setOnClickListener {
            dialog.dismiss()
            findCurrentLesson(lesson)
        }
        val inset = InsetDrawable(
            ColorDrawable(Color.TRANSPARENT),
            24.toPx(), 0, 24.toPx(), 0
        )
        dialog.window?.setBackgroundDrawable(inset);
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    private fun findCurrentLesson(lesson: Lesson? = null) {
        if (lesson != null) {
            val position = lessonsListAdapter.getPosition(
                viewModel.findLessonInProgress() ?: lesson
            )
            rv_lessons.smoothScrollToPosition(
                position
            )
            val list = viewModel.lessonsFromDb.value?.mapIndexed { index, lesson ->
                if (index == position)
                    lesson.isShowForUser = true
                lesson
            } ?: mutableListOf()
            lessonsListAdapter.submitList(list)
            lessonsListAdapter.notifyDataSetChanged()
        } else {
            val position = lessonsListAdapter.getPosition(
                viewModel.findLessonInProgress()!!
            )
            rv_lessons.smoothScrollToPosition(
                position
            )
            Handler().postDelayed({
                navItem = lessonsListAdapter.getItemOrNull(position)
                viewModel.getTasksByLesson(lessonsListAdapter.getItemOrNull(position)!!)
            }, 200)
        }
    }

    private fun showDoneLessonDialog(lesson: Lesson) {
        val dialogBinding: DialogLessonDoneBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_lesson_done,
                null,
                false
            )
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        dialogBinding.tvStart.setOnClickListener {
            dialog.dismiss()
            findCurrentLesson(lesson)
        }
        val inset = InsetDrawable(
            ColorDrawable(Color.TRANSPARENT),
            24.toPx(), 0, 24.toPx(), 0
        )
        dialog.window?.setBackgroundDrawable(inset);
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    private fun showLevelUpDialog(newLevel: Int, oldLevel: Int) {
        val dialogBinding: DialogLevelUpBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dialog_level_up,
                null,
                false
            )
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        dialogBinding.tvStart.setOnClickListener {
            dialog.dismiss()
        }
        lifecycleScope.launch {
            dialogBinding.tvLevel.apply {
                text = oldLevel.toString()
                fadeOut(1000)
            }
            delay(1000)
            dialogBinding.tvNewLevel.apply {
                text = newLevel.toString()
                fadeIn(1000)
                val endSize = 42f
                val animationDuration = 600 // Animation duration in ms

                val animator: ValueAnimator = ObjectAnimator.ofFloat(this, "textSize", endSize)
                animator.duration = animationDuration.toLong()

                animator.start()
                startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.shake));

            }
        }

        val inset = InsetDrawable(
            ColorDrawable(Color.TRANSPARENT),
            24.toPx(), 0, 24.toPx(), 0
        )
        dialog.window?.setBackgroundDrawable(inset);
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).setNavBarVisibility(true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRvAdapter()
        cl_level.setOnClickListener {
            findNavController().navigate(R.id.profile_fragment)
        }
        viewModel.getUserModel()
        viewModel.lessonsFromDb.observe(viewLifecycleOwner, Observer {
            if (it != null)
                lessonsListAdapter.submitList(it)
        })
        viewModel.isLessonReady.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                navItem?.let {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeToLessonDialog(
                            it,
                            viewModel.taskNav.toTypedArray()
                        )
                    )
                }
                viewModel.taskNav.clear()
                viewModel.isLessonReady.value = false

            }

        })
        viewModel.user.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setUserData(it)
            }
        })

        viewModel.userProgress.observe(viewLifecycleOwner, {
            if (it.toInt() >= 100) {
                val userLevel = viewModel.user.value?.level ?: 1
                val newLevel = userLevel + 1
                val points = it.toInt() - 100
                showLevelUpDialog(newLevel, userLevel)
                viewModel.updateUser(newLevel, points)
            } else if (it.toInt() != 0) {
                saveDataIntoFirebaseDatabase()

            }
        })
        viewModel.tasksDonePercent.observe(viewLifecycleOwner, Observer {
            if (it != null && it != 0f) {
                lessonsListAdapter.tasksDonePercent.postValue(it)
            }
        })
        (activity as MainActivity).setBottomBarClick {
            findNavController().navigate(HomeFragmentDirections.actionHomeToLearn())
        }
        (activity as MainActivity).setFabClick {
            findCurrentLesson()
        }
        GlobalEventController.getGlobalEvent(Constants.IS_TASK_DONE).observe(viewLifecycleOwner,
            EventObserver {
                if (it as? Boolean == true) {
                    viewModel.updateLessonData()

                }
            })
    }


    private fun setUserData(user: User) {
        viewModel.points.value = user.level.toString()
        viewModel.userProgress.value = user.progress.toString()
    }

    fun initRvAdapter() {
        val manager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        manager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // 2 column size for first row
                return if (position % 3 == 0) 2 else 1
            }

        }
        rv_lessons?.apply {
            layoutManager = manager
            adapter = lessonsListAdapter
        }
    }

    private fun getData() {
        showProgress()
        val databaseReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://dyuandrolearn.firebaseio.com/")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataModel =
                    dataSnapshot.getValue(DyuData::class.java)
                dataModel?.let {
                    viewModel.toDbLessons(dataModel)
                }
                preferenceStorage?.setIsDataLoaded(true)
                getUserData()
            }
        })
    }

    fun getUserData() {
        GlobalScope.launch(Dispatchers.Main) {

            mAuth.currentUser?.let {
                mDatabaseReference?.child(it.uid)
                    ?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            hideProgress()
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            val user: UserModel? = p0.getValue(UserModel::class.java)

                            user?.let { userModel ->
                                viewModel.convertToDbUserAndInsert(userModel)
                            }
                            hideProgress()
                        }
                    })
            }
        }
    }

    private fun saveDataIntoFirebaseDatabase() {
        val user = mAuth.currentUser
        showProgress()
        val userModel = viewModel.user.value
        user?.uid?.let { mDatabaseReference?.child(it)?.setValue(userModel) }
        hideProgress()
    }
}