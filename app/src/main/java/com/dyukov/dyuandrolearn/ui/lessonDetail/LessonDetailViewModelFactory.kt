package com.dyukov.dyuandrolearn.ui.lessonDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dyukov.dyuandrolearn.data.db.LessonsRepository

@Suppress("UNCHECKED_CAST")
class LessonDetailViewModelFactory(private val repository: LessonsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LessonDetailViewModel(repository) as T
    }
}