package com.dyukov.dyuandrolearn.ui.dialogLessonDetail

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dyukov.dyuandrolearn.data.db.Repository
import com.dyukov.dyuandrolearn.ui.home.HomeViewModel

@Suppress("UNCHECKED_CAST")
class LessonDetailViewModelFactory(
    private val repository: Repository,
    private val context: Context,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LessonDetailViewModel(repository, context) as T
    }
}