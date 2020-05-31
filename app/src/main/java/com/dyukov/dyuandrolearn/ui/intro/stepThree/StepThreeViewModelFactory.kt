package com.dyukov.dyuandrolearn.ui.intro.stepThree

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dyukov.dyuandrolearn.ui.intro.stepFour.StepFourViewModel

@Suppress("UNCHECKED_CAST")
class StepThreeViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StepThreeViewModel() as T
    }
}