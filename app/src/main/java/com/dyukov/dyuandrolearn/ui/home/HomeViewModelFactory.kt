package com.dyukov.dyuandrolearn.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dyukov.dyuandrolearn.data.db.LessonsRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val repository: LessonsRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}