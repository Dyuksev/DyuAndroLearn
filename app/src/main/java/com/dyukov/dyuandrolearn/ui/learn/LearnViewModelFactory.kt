package com.dyukov.dyuandrolearn.ui.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dyukov.dyuandrolearn.data.db.Repository

@Suppress("UNCHECKED_CAST")
class LearnViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LearnViewModel(repository) as T
    }
}