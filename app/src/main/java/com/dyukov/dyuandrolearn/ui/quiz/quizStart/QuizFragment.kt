package com.dyukov.dyuandrolearn.ui.quiz.quizStart

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
import androidx.navigation.fragment.findNavController
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.databinding.FragmentQuizStartBinding
import com.dyukov.dyuandrolearn.extensions.gone
import com.dyukov.dyuandrolearn.extensions.isEnabled
import com.dyukov.dyuandrolearn.extensions.visible
import com.dyukov.dyuandrolearn.global.Event
import com.dyukov.dyuandrolearn.global.GlobalEventController
import com.dyukov.dyuandrolearn.ui.MainActivity
import com.dyukov.dyuandrolearn.ui.quiz.quizExercise.QuizExerciseFragmentArgs
import com.dyukov.dyuandrolearn.utils.Constants
import kotlinx.android.synthetic.main.fragment_quiz_start.*
import java.util.*
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.dyukov.dyuandrolearn.ui.customViews.ImageGetter
import com.dyukov.dyuandrolearn.utils.TextOptions
import com.dyukov.dyuandrolearn.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class QuizFragment :
    BaseFragment<QuizViewModel, FragmentQuizStartBinding, QuizViewModelFactory>() {

    var playerSuccess: MediaPlayer? = null
    var timerDownMediaPlayer: MediaPlayer? = null
    lateinit var args: QuizFragmentArgs
    override fun viewModelClass(): Class<QuizViewModel> = QuizViewModel::class.java

    override fun viewModelFactory(): QuizViewModelFactory =
        QuizViewModelFactory(lessonsRepository)

    override fun layoutResId(): Int = R.layout.fragment_quiz_start


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args = QuizFragmentArgs.fromBundle(requireArguments())
        viewModel.initData(args.task)
        viewModel.theoryId = args.theoryId

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFirstQuizType()
        setSecondQuizType()
        setThirdQuizType()
        observeScore()
        startCounting()

        bt_finish.setOnClickListener {
            // GlobalEventController.getGlobalEvent(Constants.IS_TASK_DONE).value = Event(true)
            findNavController().popBackStack(R.id.home_fragment, false)
        }
        iv_close.setOnClickListener {
            findNavController().popBackStack(R.id.home_fragment, false)
        }

        viewModel.timerProgress.observe(viewLifecycleOwner, {
            if (it <= 120 && it > 60) {
                pb_time_progress.progressColor =
                    ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                pb_time_progress.progress = it
            } else if (it <= 60 && it > 30) {
                pb_time_progress.progressColor =
                    ContextCompat.getColor(requireContext(), R.color.orange)
                pb_time_progress.progress = it
            } else if (it <= 30 && it > 0) {
                if (timerDownMediaPlayer == null) {
                    timerDownMediaPlayer = MediaPlayer.create(context, R.raw.timer)
                    timerDownMediaPlayer?.start()
                }
                pb_time_progress.progressColor =
                    ContextCompat.getColor(requireContext(), R.color.red)
                pb_time_progress.progress = it
            } else if (it <= 0) {
                if (timerDownMediaPlayer?.isPlaying == true) {
                    timerDownMediaPlayer?.stop()
                    timerDownMediaPlayer?.release()
                }
                viewModel.setScoreAndEnd(viewModel.isAnswerRight)
            }
        })
        viewModel.currentExercise.observe(viewLifecycleOwner, {
            if (it.type == 1) {
                displayHtml(textView, it.question.orEmpty())
                if (it.answers?.size ?: 0 > 2){
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
            } else if  (it.type == 3) {
                displayHtml(tv_goal, it.question.orEmpty())
                cv_3.setCode(it.answers?.get(2)?.text.orEmpty())
                cv_4.setCode(it.answers?.get(3)?.text.orEmpty())
            }
        })
    }

    private fun displayHtml(textView: TextView,html: String) {
        val imageGetter = ImageGetter(resources, textView)
        val styledText = HtmlCompat.fromHtml(
            html,
            HtmlCompat.FROM_HTML_MODE_LEGACY,
            imageGetter, null
        )

        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = styledText
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

    private fun startCounting() {
        lifecycleScope.launch {
            val mp: MediaPlayer = MediaPlayer.create(context, R.raw.timer_start)
            mp.start()
            delay(2500)
            mp.stop()
        }
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
                    showAnswer()
                    playSuccess()
                } else playError()
                viewModel.setScoreAndNext(viewModel.isAnswerRight)
                handleClickable()
                setDefaultState()
            }
        })
    }

    private fun observeScore() {
        viewModel.isShowScore.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Utils.hideKeyboard(editText)
                viewModel.updateTask()
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
                showAnswer()
                playSuccess()
            } else playError()
            viewModel.setScoreAndNext(viewModel.isAnswerRight)
            handleClickable()
            setDefaultState()
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
                    showAnswer()
                    playSuccess()
                } else playError()
                viewModel.setScoreAndNext(viewModel.isAnswerRight)
                handleClickable()
                setDefaultState()
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
        lifecycleScope.launch {
            tv_res.visible()
            tv_res.alpha = 0f
            tv_res.animate().setDuration(500).alpha(1.0f)
            delay(1000)
            tv_res.animate().setDuration(500).alpha(0f)
            delay(500)
            tv_res.gone()
        }

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
        viewModel.selectedAnswerPosition.value = -1
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

    private fun playSuccess() {
        lifecycleScope.launch {
            if (playerSuccess == null) {
                playerSuccess = MediaPlayer.create(context, R.raw.correct)
                playerSuccess?.start()
                delay(1000)
                playerSuccess?.stop()
                playerSuccess = null
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

    private fun handleAllCheckbox(isClickable: Boolean) {
        cb_1.isClickable = isClickable
        cb_2.isClickable = isClickable
        cb_3.isClickable = isClickable
        cb_4.isClickable = isClickable
    }
}