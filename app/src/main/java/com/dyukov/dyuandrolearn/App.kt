package com.dyukov.dyuandrolearn

import android.app.Application
import com.dyukov.dyuandrolearn.data.db.DyuAndroDatabase
import com.dyukov.dyuandrolearn.ui.login.LoginViewModelFactory
import com.dyukov.dyuandrolearn.ui.main.MainViewModelFactory
import com.dyukov.dyuandrolearn.ui.registration.RegistrationViewModelFactory
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
        bind() from provider { MainViewModelFactory(instance()) }
        bind() from provider { LoginViewModelFactory() }
        bind() from provider { RegistrationViewModelFactory() }
    }
}