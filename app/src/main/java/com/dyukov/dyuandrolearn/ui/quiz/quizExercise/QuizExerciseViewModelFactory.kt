package com.dyukov.dyuandrolearn.ui.quiz.quizExercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dyukov.dyuandrolearn.data.db.Repository

@Suppress("UNCHECKED_CAST")
class QuizExerciseViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuizExerciseViewModel(repository) as T
    }
}