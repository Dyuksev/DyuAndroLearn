package com.dyukov.dyuandrolearn.ui.intro.stepOne

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dyukov.dyuandrolearn.ui.intro.stepFour.StepFourViewModel

@Suppress("UNCHECKED_CAST")
class StepOneViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StepOneViewModel() as T
    }
}