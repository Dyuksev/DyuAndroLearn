package com.dyukov.dyuandrolearn.ui.taskDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dyukov.dyuandrolearn.data.db.Repository

@Suppress("UNCHECKED_CAST")
class TheoryViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TheoryViewModel(repository) as T
    }
}