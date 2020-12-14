package com.dyukov.dyuandrolearn.ui.dialogLessonDetail

import android.animation.ObjectAnimator
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dyukov.dyuandrolearn.DyuApp
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseDialogFragment
import com.dyukov.dyuandrolearn.databinding.DialogLessonDetailBinding
import com.dyukov.dyuandrolearn.ui.quiz.quizExercise.QuizExerciseFragmentArgs
import com.dyukov.dyuandrolearn.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dialog_lesson_detail.*

class LessonDetailDialogFragment :
    BaseDialogFragment<LessonDetailViewModel, DialogLessonDetailBinding, LessonDetailViewModelFactory>() {
    override fun viewModelClass(): Class<LessonDetailViewModel> = LessonDetailViewModel::class.java
    lateinit var args: LessonDetailDialogFragmentArgs
    override fun viewModelFactory(): LessonDetailViewModelFactory =
        LessonDetailViewModelFactory(lessonsRepository, requireContext())

    override fun layoutResId(): Int = R.layout.dialog_lesson_detail

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(
            requireContext(),
            R.style.ServicesDialogTheme
        )//super.onCreateDialog(savedInstanceState, R.style.FullScreenDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        //  dialog.window?.attributes?.windowAnimations = R.style.ServicesDialogAnimation
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = LessonDetailDialogFragmentArgs.fromBundle(requireArguments())
        viewModel.tasks.value = ArrayList(args.tasks.toList())
        viewModel.initData(args.lesson)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isDataLoaded.observe(viewLifecycleOwner, Observer {
            if (it != null)
                ObjectAnimator.ofInt(
                    line_progress,
                    "progress",
                    viewModel.taskDonePercent
                )
                    .setDuration(1000)
                    .start()
        })

        viewModel.isOpenTask.observe(viewLifecycleOwner, {
            if (it == true) {
                viewModel.targetTask?.let {
                    findNavController().navigate(
                        LessonDetailDialogFragmentDirections.actionLessonDetailDialogFragmentToQuizExerciseFragment(
                            it,
                            viewModel.lesson.theoryId ?: 0
                        )
                    )

                    viewModel.isOpenTask.value = false

                }

            }

        })
        viewModel.isOpenTheory.observe(viewLifecycleOwner, {
            if (it == true) {
                val bundle = Bundle()
                bundle.putInt(Constants.KEY_ID_TASK, viewModel.lesson.theoryId ?: 0)
                bundle.putParcelable(Constants.KEY_TARGET_TASK, viewModel.targetTask!!)
                findNavController().navigate(R.id.tast_detail_fragment, bundle)
                viewModel.isOpenTask.value = false
            }
        })
    }
}