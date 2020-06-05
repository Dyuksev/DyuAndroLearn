package com.dyukov.dyuandrolearn.ui.home

import androidx.lifecycle.MutableLiveData
import com.dyukov.dyuandrolearn.base.BaseViewModel
import com.dyukov.dyuandrolearn.data.db.Repository
import com.dyukov.dyuandrolearn.data.db.model.Lesson
import com.dyukov.dyuandrolearn.data.db.model.Task
import com.dyukov.dyuandrolearn.data.db.model.User
import com.dyukov.dyuandrolearn.data.network.LessonModel
import com.dyukov.dyuandrolearn.data.network.TaskModel
import kotlinx.coroutines.*

class HomeViewModel(private val repository: Repository) : BaseViewModel() {
    private val lessons: ArrayList<Lesson> = ArrayList()
    private val tasks: ArrayList<Task> = ArrayList()
    var user = MutableLiveData<User>()
    val taskFromDb = MutableLiveData<List<Task>>()
    val points = MutableLiveData("")
    val progresString = MutableLiveData("")
    lateinit var lesson: Lesson

    fun toDbLessons(lessonModels: ArrayList<LessonModel>, taskModel: ArrayList<TaskModel>) {
        lessonModels.forEach {
            val tasksInt = ArrayList<Int>()
            it.tasks?.let {
                it.forEach { task ->
                    tasksInt.add(task.id ?: 0)
                }
            }
            val lesson = Lesson(
                name = it.name.orEmpty(),
                description = it.description.orEmpty(),
                tasks = tasksInt,
                points = it.points ?: 0,
                type = it.type ?: 0,
                done = it.done ?: false
            )
            lessons.add(lesson)
        }
        taskModel.forEach {
            val task = Task(
                name = it.name.orEmpty(),
                points = it.points ?: 0,
                type = it.type ?: 0,
                done = it.done,
                composition = it.composition.toString(),
                suggested = it.suggested
            )
            tasks.add(task)
        }
        insertAllTasks(tasks)
        insertAllLessons(lessons)

    }


    fun insertAllLessons(lesson: ArrayList<Lesson>) {
        GlobalScope.launch(Dispatchers.IO) {
            lesson.forEach {
                repository.insert(it)
            }
        }
    }

    fun insertAllTasks(lesson: ArrayList<Task>) {
        GlobalScope.launch(Dispatchers.IO) {
            lesson.forEach {
                repository.insert(it)
            }

        }
    }

    fun getSuggestedTasks() {
        GlobalScope.launch(Dispatchers.IO) {
            val list: ArrayList<Lesson> = ArrayList<Lesson>()
            val tasks: ArrayList<Task> = ArrayList<Task>()
            for (lesson in repository.getAllLessons()) {
                list.add(lesson)
            }

            for (lesson in list) {
                for (task in lesson.tasks) {
                    val dbTask = repository.getTasksFromLesson(task)
                    if (dbTask.suggested)
                        tasks.add(dbTask)
                }
            }
            taskFromDb.postValue(tasks)
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
}