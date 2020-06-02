package com.dyukov.dyuandrolearn.ui.lessonDetail

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseViewModel
import com.dyukov.dyuandrolearn.data.db.LessonsRepository
import com.dyukov.dyuandrolearn.data.db.model.Lesson
import com.dyukov.dyuandrolearn.data.db.model.Task
import com.dyukov.dyuandrolearn.data.db.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LessonDetailViewModel(private val repository: LessonsRepository) : BaseViewModel() {

    val taskFromDb = MutableLiveData<List<Task>>()
    val tasksSize = MutableLiveData("")
    val lessonsFromDb = MutableLiveData<Lesson>()
    var user = MutableLiveData<User>()
    var progress = MutableLiveData(0)
    var completedTasks = MutableLiveData("")
    var isLessonUpdated = MutableLiveData(false)
    var isTaskUpdated = MutableLiveData(false)
    val points = Transformations.map(user) {
        user.value?.level.toString()
    }

    var lessonId = 0

    fun getTasksByLesson() {
        GlobalScope.launch(Dispatchers.IO) {
            val tasks: ArrayList<Task> = ArrayList<Task>()
            val lesson = repository.getLesson(lessonId)
            lessonsFromDb.postValue(lesson)
            lesson.tasks.let {
                for (task in lesson.tasks) {
                    tasks.add(repository.getTasksFromLesson(task))
                }
                taskFromDb.postValue(tasks)
            }
        }
    }

    fun getUserModel() {
        GlobalScope.launch(Dispatchers.IO) {
            val userDb = repository.getUserData()
            withContext(Dispatchers.Main) {
                user.value = userDb
            }
        }
    }

    fun countCompletedTasks(context: Context) {
        var count = 0
        val size = taskFromDb.value?.size ?: 0
        val pointPerTask = ArrayList<Int>()
        taskFromDb.value?.forEach {
            if (it.done) {
                count++
                pointPerTask.add(it.points)
            }
        }
        if (size == count) {
            updateLesson()
        } else if (count < size) {
            updateUserProgressByTask(count, pointPerTask)
        }
        progress.value = (PROGRESS_MAX.div(size)).times(count)
        completedTasks.value =
            context.getString(
                R.string.tv_completed_tasks,
                count.toString(), size.toString()
            )
        tasksSize.value = context.getString(R.string.tv_lessons_contains, size.toString())
    }

    fun updateLesson() {
        GlobalScope.launch(Dispatchers.IO) {
            lessonsFromDb.value?.let {
                val updatedLesson =
                    Lesson(
                        it.name,
                        it.description,
                        it.tasks,
                        it.points,
                        it.type,
                        true,
                        it.id
                    )
                repository.update(updatedLesson)
                updateUserProgress()
            }
        }
    }

    fun updateUserProgress() {
        GlobalScope.launch(Dispatchers.IO) {
            user.value?.let {
                var currentLevel = it.level
                var currentPoints = it.progress.plus(lessonsFromDb.value?.points ?: 0)

                if (currentPoints >= PROGRESS_MAX) {
                    currentLevel++
                    currentPoints -= PROGRESS_MAX
                }
                val userUpdated = User(it.name, it.email, currentLevel, currentPoints, it.id)
                repository.update(userUpdated)

                isLessonUpdated.postValue(true)
            }
        }
    }

    fun updateUserProgressByTask(count: Int, points: ArrayList<Int>) {
        GlobalScope.launch(Dispatchers.IO) {
            user.value?.let {
                var currentLevel = it.level
                var takenPoints = 0
                for (p in points)
                    takenPoints += p
                var currentPoints = it.progress.plus(takenPoints)

                if (currentPoints >= PROGRESS_MAX) {
                    currentLevel++
                    currentPoints -= PROGRESS_MAX
                }
                val userUpdated = User(it.name, it.email, currentLevel, currentPoints, it.id)
                repository.update(userUpdated)
            }
        }
    }

    companion object {
        const val PROGRESS_MAX = 100
    }

}