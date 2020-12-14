package com.dyukov.dyuandrolearn.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View

import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseActivity
import com.dyukov.dyuandrolearn.data.db.Repository
import com.dyukov.dyuandrolearn.databinding.ActivitySplashBinding
import com.dyukov.dyuandrolearn.ui.MainActivity
import org.kodein.di.generic.instance

class SplashActivity :
    BaseActivity<SplashViewModel, ActivitySplashBinding, SplashViewModelFactory>() {

    override fun viewModelClass(): Class<SplashViewModel> = SplashViewModel::class.java

    val lessonsRepository by instance<Repository>()
    override fun viewModelFactory(): SplashViewModelFactory =
        SplashViewModelFactory()

    override fun layoutResId(): Int = R.layout.activity_splash
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}
