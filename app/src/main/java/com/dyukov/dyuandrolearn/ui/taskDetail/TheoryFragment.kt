package com.dyukov.dyuandrolearn.ui.taskDetail

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.databinding.FragmentTaskDetailBinding
import com.dyukov.dyuandrolearn.extensions.toPx
import com.dyukov.dyuandrolearn.ui.MainActivity
import com.dyukov.dyuandrolearn.ui.customViews.ImageGetter
import com.dyukov.dyuandrolearn.utils.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_task_detail.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


class TheoryFragment :
    BaseFragment<TheoryViewModel, FragmentTaskDetailBinding, TheoryViewModelFactory>() {

    override fun viewModelClass(): Class<TheoryViewModel> = TheoryViewModel::class.java

    override fun viewModelFactory(): TheoryViewModelFactory =
        TheoryViewModelFactory(lessonsRepository)

    override fun layoutResId(): Int = R.layout.fragment_task_detail

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setNavBarVisibility(false)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.theoryId = requireArguments().getInt(Constants.KEY_ID_TASK)
            viewModel.targetTask = requireArguments().getParcelable(Constants.KEY_TARGET_TASK)
            viewModel.isNextTaskAvailable.value = viewModel.targetTask != null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTheory()
        setData()
        initView()

        viewModel.isStartTaskClicked.observe(viewLifecycleOwner, {
            if (it == true) {
                lifecycleScope.launch {

                    viewModel.targetTask?.let {
                        findNavController().navigate(
                            TheoryFragmentDirections.actionTaskDetailToQuizExerciseFragment(
                                it,
                                viewModel.theoryId
                            )
                        )
                        viewModel.isStartTaskClicked.value = false
                    }
                }
            }

        })
    }

    @SuppressLint("NewApi")
    fun initView() {
        iv_back.setOnClickListener {
            findNavController().popBackStack()
        }
        sv_content.post {
            sv_content.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (scrollY > v.height - 100.toPx()) {
                    if (!viewModel.isTheoryDone) {
                        val randomPoints = Random.nextInt(10, 30)
                        viewModel.addPrizeForTheory(randomPoints)
                        showCongratulationSnackBar(randomPoints)
                        viewModel.isTheoryDone = true
                    }
                }
            }

        }

    }

    private fun showCongratulationSnackBar(randomPoint: Int) {
        Snackbar.make(
            motion_layout,
            "Вітаємо, ви отримали $randomPoint балів!",
            Snackbar.LENGTH_SHORT
        ).show();
    }

    private fun setData() {
        viewModel.theory.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                displayHtml(it.composition.orEmpty())

            }
        })
    }

    private fun displayHtml(html: String) {
        val imageGetter = ImageGetter(resources, tv_main_text)
        val styledText = HtmlCompat.fromHtml(
            html,
            HtmlCompat.FROM_HTML_MODE_LEGACY,
            imageGetter, null
        )

        tv_main_text.movementMethod = LinkMovementMethod.getInstance()
        tv_main_text.text = styledText
    }

}