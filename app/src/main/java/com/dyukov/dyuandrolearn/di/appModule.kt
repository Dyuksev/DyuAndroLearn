package com.dyukov.dyuandrolearn.di

import com.dyukov.dyuandrolearn.ui.MainViewModelFactory
import com.dyukov.dyuandrolearn.ui.home.HomeViewModelFactory
import com.dyukov.dyuandrolearn.ui.intro.IntroViewModelFactory
import com.dyukov.dyuandrolearn.ui.learn.LearnViewModelFactory
import com.dyukov.dyuandrolearn.ui.lessonDetail.LessonDetailViewModelFactory
import com.dyukov.dyuandrolearn.ui.login.LoginViewModelFactory
import com.dyukov.dyuandrolearn.ui.quiz.quizExercise.QuizExerciseViewModelFactory
import com.dyukov.dyuandrolearn.ui.quiz.quizStart.QuizViewModelFactory
import com.dyukov.dyuandrolearn.ui.registration.RegistrationViewModelFactory
import com.dyukov.dyuandrolearn.ui.taskDetail.TheoryViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

const val APP_MODULE_NAME = "APP Module"
val appModule = Kodein.Module(APP_MODULE_NAME, false) {
    bind() from provider { MainViewModelFactory(instance()) }
    bind() from provider { LoginViewModelFactory() }
    bind() from provider { RegistrationViewModelFactory() }
    bind() from provider { IntroViewModelFactory(instance()) }
    bind() from provider { LearnViewModelFactory(instance()) }
    bind() from provider { TheoryViewModelFactory(instance()) }
    bind() from provider { LessonDetailViewModelFactory(instance()) }
    bind() from provider { HomeViewModelFactory(instance()) }
    bind() from provider { QuizExerciseViewModelFactory(instance()) }
    bind() from provider { QuizViewModelFactory(instance()) }
}
