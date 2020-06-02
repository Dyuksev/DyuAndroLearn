package com.dyukov.dyuandrolearn

import android.app.Application
import com.dyukov.dyuandrolearn.data.db.DyuAndroDatabase
import com.dyukov.dyuandrolearn.data.db.LessonsRepository
import com.dyukov.dyuandrolearn.data.network.UserModel
import com.dyukov.dyuandrolearn.di.firebaseModule
import com.dyukov.dyuandrolearn.ui.login.LoginViewModelFactory
import com.dyukov.dyuandrolearn.ui.MainViewModelFactory
import com.dyukov.dyuandrolearn.ui.home.HomeViewModelFactory
import com.dyukov.dyuandrolearn.ui.intro.IntroViewModelFactory
import com.dyukov.dyuandrolearn.ui.learn.LearnViewModel
import com.dyukov.dyuandrolearn.ui.learn.LearnViewModelFactory
import com.dyukov.dyuandrolearn.ui.lessonDetail.LessonDetailViewModelFactory
import com.dyukov.dyuandrolearn.ui.registration.RegistrationViewModelFactory
import com.dyukov.dyuandrolearn.ui.taskDetail.TaskDetailViewModelFactory
import com.dyukov.dyuandrolearn.utils.PreferenceStorage
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class DyuApp : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@DyuApp))

        bind() from singleton { DyuAndroDatabase(instance()) }
        bind() from singleton { LessonsRepository(instance()) }
        bind() from singleton { UserModel() }
        bind() from singleton { PreferenceStorage(instance()) }

        bind() from provider { MainViewModelFactory(instance()) }
        bind() from provider { LoginViewModelFactory() }
        bind() from provider { RegistrationViewModelFactory() }
        bind() from provider { IntroViewModelFactory(instance()) }
        bind() from provider { LearnViewModelFactory(instance()) }
        bind() from provider { TaskDetailViewModelFactory(instance()) }
        bind() from provider { LessonDetailViewModelFactory(instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }
        import(firebaseModule)
    }
}