package com.dyukov.dyuandrolearn.ui.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dyukov.dyuandrolearn.data.db.LessonsRepository

@Suppress("UNCHECKED_CAST")
class IntroViewModelFactory(
    private val repository: LessonsRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return IntroViewModel(repository) as T
    }
}