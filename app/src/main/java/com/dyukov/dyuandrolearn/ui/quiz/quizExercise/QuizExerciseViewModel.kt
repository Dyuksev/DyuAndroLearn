package com.dyukov.dyuandrolearn.ui.quiz.quizExercise

import androidx.lifecycle.MutableLiveData
import com.dyukov.dyuandrolearn.base.BaseViewModel
import com.dyukov.dyuandrolearn.data.db.Repository
import com.dyukov.dyuandrolearn.data.db.model.PracticeData
import com.dyukov.dyuandrolearn.data.db.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class QuizExerciseViewModel(private val repository: Repository) : BaseViewModel() {

    val exercisesList = MutableLiveData<MutableList<PracticeData>>()
    val task = MutableLiveData<Task>()
    var questionSize = 0
    var theoryId = 0
    val text = MutableLiveData<String>()
    val progress = HashMap<PracticeData, Boolean>()
    val currentExercise = MutableLiveData<PracticeData>()
    val isShowScore = MutableLiveData<Boolean>()
    val isFailed = MutableLiveData<Boolean>()
    val scorePercentStr = MutableLiveData("")
    val pointEarnedStr = MutableLiveData("")
    val maxProgress = MutableLiveData(0f)
    var primaryPointPerTask = 0f
    var pointPerTask = 0f
    private var pointEarned = 0

    val scorePercent = MutableLiveData(0)
    var streakValue = 0
    var currentExercisePosition = 0
    var isAnswerRight = false


    val selectedAnswerPosition = MutableLiveData(-1)
    val selectedCodePosition = MutableLiveData(-1)


    fun initData(lessonTask: Task) {
        lessonTask.apply {
            task.value = this
            questionSize = data?.size ?: 1
            exercisesList.value = data
            currentExercise.value = data?.get(0)

            val primaryCount = data?.filter { it.primary == true }?.size ?: 0
            val usuallyCount = (data?.size ?: 0) - primaryCount
            val primaryPoints = points?.times(0.6f) ?: 0f
            primaryPointPerTask = primaryPoints.div(primaryCount)
            pointPerTask = (points?.minus(primaryPoints))?.div(usuallyCount)!!

            maxProgress.value = primaryCount * primaryPointPerTask + usuallyCount * primaryPointPerTask

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


    private fun calculateScore() {
        var sum = 0f
        progress.forEach {
            if (it.value)
                sum += 1
        }
        scorePercent.value = (sum.div(questionSize) * 100).toInt()
        scorePercentStr.value = "Ви відповіли правильно на  ${sum.toInt()}/$questionSize питань"

        pointEarned = task.value?.points?.div(questionSize)?.times(sum)?.roundToInt() ?: 0
        pointEarnedStr.value = "Ви отримали $pointEarned балів!"
        updateTask()
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
                    doneLessonsId = newDoneLessonsList,
                    progress = newPoint
                )
            )
        }
    }
}