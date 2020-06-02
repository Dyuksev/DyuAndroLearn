package com.dyukov.dyuandrolearn.ui.taskDetail

import androidx.lifecycle.MutableLiveData
import com.dyukov.dyuandrolearn.base.BaseViewModel
import com.dyukov.dyuandrolearn.data.db.LessonsRepository
import com.dyukov.dyuandrolearn.data.db.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskDetailViewModel(private val repository: LessonsRepository) : BaseViewModel() {

    val task = MutableLiveData<Task>()
    val isFromRecommend = MutableLiveData(false)
    val isDataInit = MutableLiveData(false)
    val nextTaskId = MutableLiveData(0)
    var taskId = 0

    fun getTask() {
        GlobalScope.launch(Dispatchers.IO) {
            val taskDb = repository.getTasksFromLesson(taskId)
            task.postValue(taskDb)
            isDataInit.postValue(true)
        }
    }

    fun updateTask() {
        GlobalScope.launch(Dispatchers.IO) {
            task.value?.let {
                val updatedTask =
                    Task(it.name, it.composition, it.points, it.type, true, it.suggested, it.id)
                repository.update(updatedTask)
            }
        }
    }

}