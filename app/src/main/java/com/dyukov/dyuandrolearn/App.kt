package com.dyukov.dyuandrolearn

import android.app.Application
import com.dyukov.dyuandrolearn.data.db.DyuAndroDatabase
import com.dyukov.dyuandrolearn.data.db.Repository
import com.dyukov.dyuandrolearn.data.network.UserModel
import com.dyukov.dyuandrolearn.di.appModule
import com.dyukov.dyuandrolearn.di.firebaseModule
import com.dyukov.dyuandrolearn.utils.PreferenceStorage
import io.github.kbiakov.codeview.classifier.CodeProcessor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class DyuApp : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@DyuApp))


        bind() from singleton { DyuAndroDatabase(instance()) }
        bind() from singleton { Repository(instance()) }
        bind() from singleton { PreferenceStorage(instance()) }

        import(appModule)
        import(firebaseModule)

        CodeProcessor.init(applicationContext)
    }
}