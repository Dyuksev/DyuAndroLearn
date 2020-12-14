package com.dyukov.dyuandrolearn.ui.quiz.quizStart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dyukov.dyuandrolearn.data.db.Repository

@Suppress("UNCHECKED_CAST")
class QuizViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuizViewModel(repository) as T
    }
}