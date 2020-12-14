package com.dyukov.dyuandrolearn.ui.quiz.quizExercise

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.databinding.FragmentQuizBinding
import com.dyukov.dyuandrolearn.extensions.gone
import com.dyukov.dyuandrolearn.extensions.isEnabled
import com.dyukov.dyuandrolearn.extensions.visible
import com.dyukov.dyuandrolearn.global.Event
import com.dyukov.dyuandrolearn.global.GlobalEventController
import com.dyukov.dyuandrolearn.ui.MainActivity
import com.dyukov.dyuandrolearn.ui.customViews.ImageGetter
import com.dyukov.dyuandrolearn.utils.Constants
import com.dyukov.dyuandrolearn.utils.TextOptions
import kotlinx.android.synthetic.main.fragment_quiz.*
import kotlinx.android.synthetic.main.fragment_task_detail.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class QuizExerciseFragment :
    BaseFragment<QuizExerciseViewModel, FragmentQuizBinding, QuizExerciseViewModelFactory>() {
    var playerSuccess: MediaPlayer? = null
    lateinit var args: QuizExerciseFragmentArgs
    var isExit = false
    override fun viewModelClass(): Class<QuizExerciseViewModel> = QuizExerciseViewModel::class.java
    override fun viewModelFactory(): QuizExerciseViewModelFactory =
        QuizExerciseViewModelFactory(lessonsRepository)

    override fun layoutResId(): Int = R.layout.fragment_quiz
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = QuizExerciseFragmentArgs.fromBundle(requireArguments())
        viewModel.initData(args.task)
        viewModel.theoryId = args.theoryId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFirstQuizType()
        observeScore()
        setSecondQuizType()
        setThirdQuizType()
        initTestFailedCase()
        bt_next.setOnClickListener {
            updatePrimaryProgress()
            playerSuccess = null
            viewModel.setScoreAndNext(viewModel.isAnswerRight)
            setDefaultState()
        }
        viewModel.currentExercise.observe(viewLifecycleOwner, {
            if (it.type == 1) {
                displayHtml(textView, it.question.orEmpty())
                if (it.answers?.size ?: 0 > 2) {
                    bt_answer_3.text = it.answers?.get(2)?.text
                    bt_answer_4.text = it.answers?.get(3)?.text
                    val textOptions = TextOptions.quizTextOptions

                    val textLength = it.answers?.get(2)?.text?.length
                    val maxTextOption = textOptions.last()
                    textOptions.sortedBy { it.length }.forEach { textOption ->
                        if (textLength ?: 0 <= textOption.length) {
                            bt_answer_3.maxLines = textOption.lines
                            bt_answer_3.setTextSize(TypedValue.COMPLEX_UNIT_SP, textOption.size)
                        } else {
                            bt_answer_3.maxLines = maxTextOption.lines
                            bt_answer_3.setTextSize(TypedValue.COMPLEX_UNIT_SP, maxTextOption.size)
                        }
                    }
                    val textLength2 = it.answers?.get(3)?.text?.length
                    val maxTextOption2 = textOptions.last()
                    textOptions.sortedBy { it.length }.forEach { textOption ->
                        if (textLength2 ?: 0 <= textOption.length) {
                            bt_answer_4.maxLines = maxTextOption2.lines
                            bt_answer_4.setTextSize(TypedValue.COMPLEX_UNIT_SP, textOption.size)
                        } else {
                            bt_answer_4.maxLines = maxTextOption2.lines
                            bt_answer_4.setTextSize(TypedValue.COMPLEX_UNIT_SP, maxTextOption.size)
                        }
                    }
                }
            } else if (it.type == 2) {
                displayHtml(tv_question_type_2, it.question.orEmpty())
            } else if (it.type == 3) {
                displayHtml(tv_goal, it.question.orEmpty())
                cv_3.setCode(it.answers?.get(2)?.text.orEmpty())
                cv_4.setCode(it.answers?.get(3)?.text.orEmpty())
            }
        })
        bt_finish.setOnClickListener {
            GlobalEventController.getGlobalEvent(Constants.IS_TASK_DONE).value = Event(true)
            findNavController().popBackStack(R.id.home_fragment, false)
        }
        iv_close.setOnClickListener {
            showSubmitDialog()
        }
        initProgressBar()
    }

    private fun displayHtml(textView: TextView, html: String) {
        val imageGetter = ImageGetter(resources, textView)
        val styledText = HtmlCompat.fromHtml(
            html,
            HtmlCompat.FROM_HTML_MODE_LEGACY,
            imageGetter, null
        )
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = styledText
    }

    private fun initProgressBar() {
        pb_progress?.let {
            it.max = viewModel.task.value?.points?.toFloat() ?: 0f
            it.progress = 0f
            it.secondaryProgress = 0f
            it.progressText = ""
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).setNavBarVisibility(false)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onStop() {
        super.onStop()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun setFirstQuizType() {
        viewModel.selectedAnswerPosition.observe(viewLifecycleOwner, Observer {
            if (it != -1) {
                when (it) {
                    1 -> {
                        checkAnswer(bt_answer_1, true)
                    }
                    2 -> {
                        checkAnswer(bt_answer_2, true)
                    }
                    3 -> {
                        checkAnswer(bt_answer_3, true)
                    }
                    4 -> {
                        checkAnswer(bt_answer_4, true)
                    }
                }
                viewModel.isAnswerRight = it == viewModel.currentExercise.value?.rightAnswer
                if (viewModel.isAnswerRight) {
                    playSuccess()
                    viewModel.streakValue = viewModel.streakValue + 1
                    val value = countStreak(viewModel.streakValue)
                    if (value != 0)
                        addDataToProgress(value)
                    else addDataToProgress()
                    tv_res.text = "Ваша відповідь правильна"
                    tv_res.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                    setNextButtonVisibility(true)
                } else {
                    playError()
                    if (viewModel.currentExercise.value?.primary == true) {
                        viewModel.isFailed.value = true
                        isExit = true
                        bt_next.gone()
                    } else {
                        viewModel.streakValue = 0
                        tv_res.text = "Ваша відповідь неправильна"
                        tv_res.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                        setNextButtonVisibility(true)
                    }
                }

                showAnswer()
                handleClickable()

            }
        })
    }

    private fun playSuccess() {
        lifecycleScope.launch {
            if (playerSuccess == null) {
                playerSuccess = MediaPlayer.create(context, R.raw.correct)
                playerSuccess?.start()
                delay(1000)
            }
        }

    }

    private fun playError() {
        lifecycleScope.launch {
            if (playerSuccess == null) {
                playerSuccess = MediaPlayer.create(context, R.raw.error)
                playerSuccess?.start()
                delay(1000)
                playerSuccess?.stop()
                playerSuccess = null
            }
        }

    }

    private fun observeScore() {
        viewModel.isShowScore.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                isExit = true
                playerSuccess?.release()
                bt_next.gone()
                pb_progress.gone()
                val mp: MediaPlayer = MediaPlayer.create(context, R.raw.victory)
                mp.start()
                val color = when (viewModel.scorePercent.value ?: 0) {
                    in 0..50 -> ContextCompat.getColor(requireContext(), R.color.red)
                    in 51..74 -> ContextCompat.getColor(requireContext(), R.color.orange)
                    else -> ContextCompat.getColor(requireContext(), R.color.green)
                }
                line_progress.setProgressTextColor(color)
                ObjectAnimator.ofInt(line_progress, "progress", viewModel.scorePercent.value ?: 0)
                    .setDuration(1000)
                    .start()
                showFinishButton()
            }
        })
    }

    private fun addDataToProgress(streak: Int = 0) {
        pb_progress.apply {
            val newPoint = if (viewModel.currentExercise.value?.primary == true)
                viewModel.primaryPointPerTask
            else viewModel.pointPerTask
            secondaryProgress =
                progress + streak + newPoint
            progressText = "+" + (newPoint.roundToInt() + streak).toString()

        }
    }

    private fun updatePrimaryProgress() {
        pb_progress.apply {
            progressText = ""
            progress = secondaryProgress
        }
    }

    private fun setSecondQuizType() {
        bt_check.setOnClickListener {
            if (editText.text.length < 1) {
                editText.error = "Відповідь повинна мати не менше 3 символів"
                return@setOnClickListener
            }
            viewModel.isAnswerRight =
                viewModel.currentExercise.value?.rightAnswerStr?.toLowerCase(Locale.getDefault()) == editText.text.toString()
                    .toLowerCase(Locale.getDefault())
            editText.isEnabled(false)
            if (viewModel.isAnswerRight) {
                playSuccess()
                viewModel.streakValue = viewModel.streakValue + 1
                val value = countStreak(viewModel.streakValue ?: 0)
                if (value != 0)
                    addDataToProgress(value)
                else addDataToProgress()
                tv_res.text = "Ваша відповідь правильна"
                tv_res.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                editText.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bg_rect_stroke_rounded_green
                )
                showAnswer()
                setNextButtonVisibility(true)
            } else {
                playError()

                if (viewModel.currentExercise.value?.primary == true) {
                    viewModel.isFailed.value = true
                    bt_next.gone()
                    isExit = true
                } else {
                    viewModel.streakValue = 0
                    tv_res.text = "Ваша відповідь неправильна"
                    tv_res.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))

                    editText.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_rect_stroke_rounded_red
                    )
                    setNextButtonVisibility(true)

                    showAnswer()
                    bt_check.gone()
                }
            }
        }
    }

    private fun setThirdQuizType() {
        cb_1.setOnCheckedChangeListener { _, _ ->
            viewModel.selectedCodePosition.value = 1
            handleAllCheckbox(false)
        }
        cb_2.setOnCheckedChangeListener { _, _ ->
            viewModel.selectedCodePosition.value = 2
            handleAllCheckbox(false)
        }
        cb_3.setOnCheckedChangeListener { _, _ ->
            viewModel.selectedCodePosition.value = 3
            handleAllCheckbox(false)
        }
        cb_4.setOnCheckedChangeListener { _, _ ->
            viewModel.selectedCodePosition.value = 4
            handleAllCheckbox(false)
        }

        viewModel.selectedCodePosition.observe(viewLifecycleOwner, Observer {
            if (it != -1) {
                when (it) {
                    1 -> checkAnswer(cl_code_1, false)
                    2 -> checkAnswer(cl_code_2, false)
                    3 -> checkAnswer(cl_code_3, false)
                    4 -> checkAnswer(cl_code_4, false)
                }
                viewModel.isAnswerRight = it == viewModel.currentExercise.value?.rightAnswer
                if (viewModel.isAnswerRight) {
                    playSuccess()
                    viewModel.streakValue = viewModel.streakValue + 1
                    val value = countStreak(viewModel.streakValue ?: 0)
                    if (value != 0)
                        addDataToProgress(value)
                    else addDataToProgress()
                    tv_res.text = "Ваша відповідь правильна"
                    tv_res.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                    setNextButtonVisibility(true)
                } else {
                    playError()
                    if (viewModel.currentExercise.value?.primary == true) {
                        viewModel.isFailed.value = true
                        bt_next.gone()
                        isExit = true
                    } else {
                        viewModel.streakValue = 0
                        tv_res.text = "Ваша відповідь неправильна"
                        setNextButtonVisibility(true)
                        tv_res.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                    }
                }
                showAnswer()
            }
        })
    }

    private fun checkAnswer(clickedView: View, boolean: Boolean) {
        val list =
            if (boolean) getRightAnswer(viewModel.currentExercise.value?.rightAnswer ?: 0)
            else getRightCodeAnswer(viewModel.currentExercise.value?.rightAnswer ?: 0)
        list.forEachIndexed { index, view ->
            if (index == 0) {
                view.background =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_rect_right
                    )

            } else {
                if (view == clickedView)
                    view.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_rect_wrong
                    )
                else
                    view.background =
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.bg_rect_grey
                        )
            }
        }
    }

    private fun countStreak(steak: Int): Int {
        when (steak) {
            in 3..6 -> {
                tv_streak.visible()
                tv_streak.text = resources.getString(R.string.streak_s, 3)
                tv_streak.alpha = 0f
                tv_streak.animate().setDuration(1000).alpha(1.0f)
                return 3
            }
            in 6..9 -> {
                tv_streak.visible()
                tv_streak.text = resources.getString(R.string.streak_s, 6)
                tv_streak.alpha = 0f
                tv_streak.animate().setDuration(1000).alpha(1.0f)
                return 6
            }
            in 9..12 -> {
                tv_streak.visible()
                tv_streak.text = resources.getString(R.string.streak_s, 9)
                tv_streak.alpha = 0f
                tv_streak.animate().setDuration(1000).alpha(1.0f)
                return 9
            }
        }
        return 0
    }

    private fun getRightAnswer(id: Int): ArrayList<View> {
        val views = ArrayList<View>()
        when (id) {
            1 -> {
                views.add(bt_answer_1)
                views.add(bt_answer_2)
                views.add(bt_answer_3)
                views.add(bt_answer_4)
            }
            2 -> {
                views.add(bt_answer_2)
                views.add(bt_answer_1)
                views.add(bt_answer_3)
                views.add(bt_answer_4)
            }
            3 -> {
                views.add(bt_answer_3)
                views.add(bt_answer_1)
                views.add(bt_answer_2)
                views.add(bt_answer_4)
            }
            4 -> {
                views.add(bt_answer_4)
                views.add(bt_answer_1)
                views.add(bt_answer_2)
                views.add(bt_answer_3)
            }
        }
        return views
    }

    private fun getRightCodeAnswer(id: Int): ArrayList<View> {
        val views = ArrayList<View>()
        when (id) {
            1 -> {
                views.add(cl_code_1)
                views.add(cl_code_2)
                views.add(cl_code_3)
                views.add(cl_code_4)
            }
            2 -> {
                views.add(cl_code_2)
                views.add(cl_code_1)
                views.add(cl_code_3)
                views.add(cl_code_4)
            }
            3 -> {
                views.add(cl_code_3)
                views.add(cl_code_1)
                views.add(cl_code_2)
                views.add(cl_code_4)
            }
            4 -> {
                views.add(cl_code_4)
                views.add(cl_code_1)
                views.add(cl_code_2)
                views.add(cl_code_3)
            }
        }
        return views
    }

    private fun handleClickable(isClickable: Boolean = false) {
        bt_answer_1.isClickable = isClickable
        bt_answer_2.isClickable = isClickable
        bt_answer_3.isClickable = isClickable
        bt_answer_4.isClickable = isClickable
    }

    private fun showAnswer() {
        tv_res.visible()
        tv_res.alpha = 0f
        tv_res.animate().setDuration(1000).alpha(1.0f)
        setNextButtonVisibility(true)
    }

    private fun showFinishButton() {
        bt_finish.visible()
        bt_finish.alpha = 0f
        bt_finish.animate().setDuration(1000).alpha(1.0f)
    }

    private fun setNextButtonVisibility(isVisible: Boolean) {
        bt_next.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setDefaultState() {
        setDefaultStateFirst()
        setDefaultStateSecond()
        setDefaultStateThird()
        setNextButtonVisibility(false)
        tv_res.gone()
        viewModel.selectedAnswerPosition.value = -1
        tv_streak.gone()
    }

    private fun setDefaultStateFirst() {
        bt_answer_1.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_rect_red)
        bt_answer_2.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_rect_yellow)
        bt_answer_3.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_rect_green)
        bt_answer_4.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_rect_blue)
        handleClickable(true)
    }

    private fun setDefaultStateSecond() {
        editText?.apply {
            background = ContextCompat.getDrawable(
                requireContext(), R.drawable.bg_rect_stroke_rounded
            )
            text.clear()
            editText.isEnabled(true)
        }
        bt_check.visible()
        tv_right_answer.gone()
    }

    private fun setDefaultStateThird() {
        handleAllCheckbox(true)
        cb_1.isChecked = false
        cb_2.isChecked = false
        cb_3.isChecked = false
        cb_4.isChecked = false
        cl_code_1.background =
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.bg_rect_grey
            )
        cl_code_2.background =
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.bg_rect_grey
            )
        cl_code_3.background =
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.bg_rect_grey
            )
        cl_code_4.background =
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.bg_rect_grey
            )
    }

    private fun initTestFailedCase() {
        bt_open_theory.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(Constants.KEY_ID_TASK, viewModel.theoryId)
            bundle.putParcelable(Constants.KEY_TARGET_TASK, viewModel.task.value)
            findNavController().navigate(R.id.tast_detail_fragment, bundle)

        }
    }

    private fun handleAllCheckbox(isClickable: Boolean) {
        cb_1.isClickable = isClickable
        cb_2.isClickable = isClickable
        cb_3.isClickable = isClickable
        cb_4.isClickable = isClickable
    }

    override fun onBackPressed(): Boolean {
        showSubmitDialog()

        return true
    }

    private fun showSubmitDialog() {
        if (!isExit)
            AlertDialog.Builder(context)
                .setTitle("Ви дійно бажаєте вийти?")
                .setMessage("У такому випадку прогрес цього завдання буде втрачено")
                .setPositiveButton(
                    "Вийти"
                ) { dialog, which ->
                    findNavController().popBackStack(R.id.home_fragment, false)
                    dialog.dismiss()
                }
                .setNegativeButton("Залишитися") { dialog, which ->
                    dialog.dismiss()
                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        else findNavController().popBackStack(R.id.home_fragment, false)
    }
}