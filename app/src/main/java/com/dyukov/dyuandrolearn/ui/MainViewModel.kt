package com.dyukov.dyuandrolearn.ui

import com.dyukov.dyuandrolearn.base.BaseViewModel
import com.dyukov.dyuandrolearn.data.db.LessonsRepository
import com.dyukov.dyuandrolearn.data.db.model.Lesson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: LessonsRepository
) : BaseViewModel() {

//    fun insertAllLessons(lesson: ArrayList<Lesson>) {
//        lesson.forEach {
//            repository.insert(it)
//        }
//    }
//
//    fun upsert(item: Lesson) = CoroutineScope(Dispatchers.Main).launch {
//        repository.insert(item)
//    }
//
//    fun delete(item: Lesson) = CoroutineScope(Dispatchers.Main).launch {
//        repository.delete(item)
//    }


}