package com.dyukov.dyuandrolearn.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseActivity
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.data.db.Repository
import com.dyukov.dyuandrolearn.databinding.ActivityMainBinding
import com.dyukov.dyuandrolearn.extensions.gone
import com.dyukov.dyuandrolearn.extensions.visible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.generic.instance

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding, MainViewModelFactory>() {

    override fun viewModelClass(): Class<MainViewModel> = MainViewModel::class.java

    val lessonsRepository by instance<Repository>()
    override fun viewModelFactory(): MainViewModelFactory =
        MainViewModelFactory(lessonsRepository)

    override fun layoutResId(): Int = R.layout.activity_main
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        if (mAuth.currentUser != null) navController.navigate(R.id.action_login_to_home)
        setBottomBar(R.id.action_lessons)
    }

    //    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when (item!!.itemId) {
//            R.id.action_lessons -> {
//                navController.navigate(R.id.learn_fragment)
//                true
//            }
//        }
//
//        return true
//    }
    fun resetBottomMenu() {
        binding.bottomAppBar.menu.getItem(0).isChecked = true;
    }

    private fun setBottomBar(@IdRes anInt: Int) {
//        bottom_app_bar.selec = anInt
//        bottom_navigation_view.setOnNavigationItemSelectedListener(this)
    }

    fun showProgress() {
        progressBar.visible()
    }

    fun hideProgress() {
        progressBar.gone()
    }

    fun setBottomBarClick(function: () -> (Unit)) {
        binding.bottomAppBar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_lessons) {
                function()
            }
            return@setOnMenuItemClickListener true
        }
    }

    fun setFabClick(function: () -> (Unit)) {
        binding.fab.setOnClickListener {
            function()
        }
    }

    fun setNavBarVisibility(isVisible: Boolean) {
        if (isVisible) {
            bottom_app_bar.visibility = View.VISIBLE
            fab.visible()
        } else {
            bottom_app_bar.visibility = View.GONE
            fab.gone()
        }
    }

    override fun onBackPressed() {
        val fragmentList: List<*> = supportFragmentManager.fragments
        var handled = false
        for (f in fragmentList) {
            if (f is BaseFragment<*, *, *>) {
                handled = f.onBackPressed()
                if (handled) {
                    break
                }
            }
        }
        if (!handled) {
            super.onBackPressed()
        }
    }
}
