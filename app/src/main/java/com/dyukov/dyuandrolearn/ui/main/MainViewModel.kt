package com.dyukov.dyuandrolearn.ui.main

import androidx.lifecycle.ViewModel
import com.dyukov.dyuandrolearn.data.db.LessonItem
import com.dyukov.dyuandrolearn.data.db.LessonsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: LessonsRepository
) : ViewModel() {
    fun upsert(item: LessonItem) = CoroutineScope(Dispatchers.Main).launch {
        repository.upsert(item)
    }

    fun delete(item: LessonItem) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(item)
    }

    fun getAllShoppingItem() = repository.getAllshoppingItem()

}