package com.dyukov.dyuandrolearn.ui.learn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dyukov.dyuandrolearn.base.BaseViewModel
import com.dyukov.dyuandrolearn.data.db.LessonsRepository
import com.dyukov.dyuandrolearn.data.db.model.Lesson
import com.dyukov.dyuandrolearn.data.db.model.Task
import com.dyukov.dyuandrolearn.data.db.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LearnViewModel(private val repository: LessonsRepository) : BaseViewModel() {

    val taskFromDb = MutableLiveData<List<Task>>()
    val lessonsFromDb = MutableLiveData<List<Lesson>>()
    var user = MutableLiveData<User>()
    val points = Transformations.map(user) {
        user.value?.level.toString()
    }

    fun getLessons() {
        GlobalScope.launch(Dispatchers.IO) {
            val list: ArrayList<Lesson> = ArrayList<Lesson>()
            val tasks: ArrayList<Task> = ArrayList<Task>()
            for (lesson in repository.getAllLessons()) {
                list.add(lesson)
            }
            lessonsFromDb.postValue(list)

            for (lesson in list) {
                for (task in lesson.tasks) {
                    tasks.add(repository.getTasksFromLesson(task))
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