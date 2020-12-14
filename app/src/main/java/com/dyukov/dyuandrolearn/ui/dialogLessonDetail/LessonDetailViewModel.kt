package com.dyukov.dyuandrolearn.ui.dialogLessonDetail

import android.app.Application
import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.dyukov.dyuandrolearn.DyuApp
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseViewModel
import com.dyukov.dyuandrolearn.data.db.Repository
import com.dyukov.dyuandrolearn.data.db.model.Lesson
import com.dyukov.dyuandrolearn.data.db.model.Task
import com.dyukov.dyuandrolearn.di.appModule
import org.kodein.di.generic.instance
import kotlin.math.roundToInt

class LessonDetailViewModel(
    private val repository: Repository,
    private val context: Context
) : BaseViewModel() {

    lateinit var lesson: Lesson
    val lessonName = MutableLiveData("")
    val tasks = MutableLiveData<ArrayList<Task>>()
    val lessonProgress = MutableLiveData("")
    var taskDonePercent = 0
    var isDataLoaded = MutableLiveData(false)

    var isOpenTask = MutableLiveData(false)
    var isOpenTheory = MutableLiveData(false)
    var targetTask: Task? = null

    fun initData(lesson: Lesson) {
        this.lesson = lesson
        lesson.apply {
            lessonName.value = name
            tasks.value?.let { task ->
                val tasksDoneSize = task.filter { it.done == true }.size
                taskDonePercent = (tasksDoneSize.toFloat().div(task.size) * 100).roundToInt()

                lessonProgress.value = context.resources.getString(
                    R.string.lessons_done_s,
                    tasksDoneSize,
                    tasks.value?.size
                )
                isDataLoaded.value = true
                targetTask = getNextTask()
            }

        }
    }

    fun onStartClick() {
        isOpenTask.value = true
    }

    fun onTheoryClick() {
        isOpenTheory.value = true
    }

    fun getNextTask(): Task? {
        tasks.value?.forEach {
            if (it.done == false)
                return it
        }
        return null
    }
}