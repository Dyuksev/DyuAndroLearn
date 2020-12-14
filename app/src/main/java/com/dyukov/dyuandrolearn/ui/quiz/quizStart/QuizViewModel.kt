package com.dyukov.dyuandrolearn.ui.quiz.quizStart

import androidx.lifecycle.MutableLiveData
import com.dyukov.dyuandrolearn.base.BaseViewModel
import com.dyukov.dyuandrolearn.data.db.Repository
import com.dyukov.dyuandrolearn.data.db.model.PracticeData
import com.dyukov.dyuandrolearn.data.db.model.Task
import kotlinx.coroutines.*
import kotlin.math.roundToInt

class QuizViewModel(private val repository: Repository) : BaseViewModel() {

    val exercisesList = MutableLiveData<MutableList<PracticeData>>()
    val task = MutableLiveData<Task>()
    var questionSize = 0
    var theoryId = 0
    val text = MutableLiveData<String>()
    val progress = HashMap<PracticeData, Boolean>()
    val currentExercise = MutableLiveData<PracticeData>()
    val isShowScore = MutableLiveData<Boolean>()
    val isFailed = MutableLiveData<Boolean>()
    var pointPerTask = 0f
    private var pointEarned = 0

    val scorePercent = MutableLiveData(0)
    var currentExercisePosition = 0
    var isAnswerRight = false

    val scorePercentStr = MutableLiveData("")
    val selectedAnswerPosition = MutableLiveData(-1)
    val selectedCodePosition = MutableLiveData(-1)
    val pointEarnedStr = MutableLiveData("")
    val timerProgress = MutableLiveData<Float>(120f)

    val readyValue = MutableLiveData("3")
    val isShowReadyFlow = MutableLiveData(true)

    fun initData(lessonTask: Task) {
        lessonTask.apply {
            task.value = this
            questionSize = data?.size ?: 1
            exercisesList.value = data
            currentExercise.value = data?.get(0)
            pointPerTask = 2f

            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                readyValue.postValue("2")
                delay(1000)
                readyValue.postValue("1")
                delay(1000)
                isShowReadyFlow.value = false
                initTimer()
            }
        }
    }

    fun initTimer() {
        CoroutineScope(Dispatchers.Default).launch {
            repeat(60) {
                timerProgress.postValue(timerProgress.value?.minus(2f))
                delay(1000)
            }
        }
    }

    fun setScoreAndNext(isAnswerRight: Boolean) {
        progress[currentExercise.value!!] = isAnswerRight
        currentExercisePosition += 1

        if (currentExercisePosition < questionSize) {
            currentExercise.value = exercisesList.value?.get(currentExercisePosition)
        } else {
            calculateScore()
            isShowScore.value = true
        }
    }

    fun setScoreAndEnd(isAnswerRight: Boolean) {
        progress[currentExercise.value!!] = isAnswerRight
        currentExercisePosition += 1
        calculateScore()
        isShowScore.value = true
    }


    private fun calculateScore() {
        var sum = 0f
        progress.forEach {
            if (it.value)
                sum += 1
        }
        scorePercent.value = (sum.div(questionSize) * 100).toInt()
        scorePercentStr.value = "Ви відповіли правильно на  ${sum.toInt()}/$questionSize питань"

        pointEarned = sum.times(2).roundToInt()
        pointEarnedStr.value = "Ви отримали $pointEarned очок!"
    }

    fun onClickAnswer(id: Int) {
        selectedAnswerPosition.value = id
    }


    fun updateTask() {
        GlobalScope.launch(Dispatchers.IO) {
            task.value?.let {
                val updatedTask =
                    Task(
                        id = it.id,
                        data = it.data,
                        theoryId = it.theoryId,
                        points = it.points,
                        isPrimary = it.isPrimary,
                        done = true
                    )
                repository.update(updatedTask)
                updateUser()
            }
        }
    }

    fun updateUser() {
        GlobalScope.launch(Dispatchers.IO) {
            val user = repository.getUserData()
            val newDoneLessonsList = user.doneLessonsId ?: arrayListOf()
            val newPoint = (user.progress ?: 0) + pointEarned
            task.value?.id?.let { newDoneLessonsList.add(it) }
            repository.update(
                user.copy(
                    progress = newPoint
                )
            )
        }
    }
}