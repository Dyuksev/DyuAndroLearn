package com.dyukov.dyuandrolearn.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dyukov.dyuandrolearn.data.db.Repository
import com.dyukov.dyuandrolearn.ui.MainViewModel

@Suppress("UNCHECKED_CAST")
class SplashViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel() as T
    }
}