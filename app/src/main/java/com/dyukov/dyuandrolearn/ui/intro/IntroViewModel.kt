package com.dyukov.dyuandrolearn.ui.intro

import androidx.lifecycle.MutableLiveData
import com.dyukov.dyuandrolearn.base.BaseViewModel
import com.dyukov.dyuandrolearn.data.db.Repository
import com.dyukov.dyuandrolearn.data.db.model.Lesson
import com.dyukov.dyuandrolearn.data.db.model.Task
import com.dyukov.dyuandrolearn.data.db.model.User
import com.dyukov.dyuandrolearn.data.network.*
import kotlinx.coroutines.*

class IntroViewModel(
    private val repository: Repository
) : BaseViewModel() {
    var currentPosition: Int = 0

    private val lessons: ArrayList<Lesson> = ArrayList()
    private val tasks: ArrayList<Task> = ArrayList()
    val taskFromDb = MutableLiveData<List<Task>>()
    lateinit var lesson: Lesson

    fun toDbLessons(appData: DyuData) {

        appData.lessons?.forEach {

            val lesson = Lesson(
                id = it.id,
                type = it.type ?: 1,
                name = it.name.orEmpty(),
                points = it.points ?: 0,
                theoryId = it.theoryId ?: 1,
                tasksId = it.tasksId ?: arrayListOf()
            )
            lessons.add(lesson)
        }
        appData.tasks?.forEach {
            val task = Task(
                id = it.id,
                data = it.data,
                theoryId = it.theoryId ?: 1,
                points = it.points ?: 0,
                isPrimary = it.primary,
                done = it.done
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

    fun convertToDbUserAndInsert(userModel: UserModel?) {
        userModel?.let {
            val user = User(it.name, it.email, it.level, it.progress, it.doneLessonsId, it.doneTheoryId)
            GlobalScope.launch(Dispatchers.IO) {
                repository.insert(user)
            }
        }
    }
}