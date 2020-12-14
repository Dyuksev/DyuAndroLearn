package com.dyukov.dyuandrolearn.ui.learn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dyukov.dyuandrolearn.base.BaseViewModel
import com.dyukov.dyuandrolearn.data.db.Repository
import com.dyukov.dyuandrolearn.data.db.model.Lesson
import com.dyukov.dyuandrolearn.data.db.model.Task
import com.dyukov.dyuandrolearn.data.db.model.Theory
import com.dyukov.dyuandrolearn.data.db.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LearnViewModel(private val repository: Repository) : BaseViewModel() {


    val theories = MutableLiveData<List<Theory>>()
    var user = MutableLiveData<User>()
    val points = Transformations.map(user) {
        user.value?.level.toString()
    }


    fun getTheories() {
        GlobalScope.launch(Dispatchers.IO) {
            val user = repository.getUserData()
            val doneTheoryList = user.doneTheoriesId.orEmpty()
            val theoryList: ArrayList<Theory> = ArrayList<Theory>()
            for (theory in repository.getAllTheories()) {
                theoryList.add(theory)
            }
            theories.postValue(theoryList.filter { it.id in doneTheoryList })
        }
    }
}