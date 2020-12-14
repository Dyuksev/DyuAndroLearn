package com.dyukov.dyuandrolearn.ui.taskDetail

import androidx.lifecycle.MutableLiveData
import com.dyukov.dyuandrolearn.base.BaseViewModel
import com.dyukov.dyuandrolearn.data.db.Repository
import com.dyukov.dyuandrolearn.data.db.model.Task
import com.dyukov.dyuandrolearn.data.db.model.Theory
import com.dyukov.dyuandrolearn.data.db.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class TheoryViewModel(private val repository: Repository) : BaseViewModel() {

    val theory = MutableLiveData<Theory>()
    val user = MutableLiveData<User>()
    val isDataInit = MutableLiveData(false)
    val isNextTaskAvailable = MutableLiveData(false)
    val isStartTaskClicked = MutableLiveData(false)
    var theoryId = 0
    var isTheoryDone = false
    var targetTask: Task? = null

    fun getTheory() {
        GlobalScope.launch(Dispatchers.IO) {
            val taskDb = repository.getTheoryById(theoryId)
            theory.postValue(taskDb)
            isDataInit.postValue(true)
            val users = repository.getUserData()
            isTheoryDone = users.doneTheoriesId?.contains(theoryId) ?: false
        }
    }

    fun addPrizeForTheory(randomPoints: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            theory.value?.earned = true
            theory.value?.copy(earned = true)?.let { repository.update(it) }
            val user = repository.getUserData()
            user.apply {
                val list = doneTheoriesId
                list?.add(theoryId)
                val updatedUser = copy(
                    progress = (progress ?: 0) + randomPoints, doneTheoriesId = list
                )
                repository.update(updatedUser)
            }
        }
    }


    fun onClickStartTask() {
        isStartTaskClicked.value = true
    }


    fun updateTask() {
//        GlobalScope.launch(Dispatchers.IO) {
//            task.value?.let {
//                val updatedTask =
//                    Task(it.theoryData, null, it.points, it.type, true, it.suggested, it.id)
//                repository.update(updatedTask)
//            }
//        }
    }
}