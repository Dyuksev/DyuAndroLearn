package com.dyukov.dyuandrolearn.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

abstract class BaseActivity<V : BaseViewModel, B : ViewDataBinding, T : ViewModelProvider.NewInstanceFactory> :
    AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    protected val mAuth: FirebaseAuth by instance<FirebaseAuth>()
    protected abstract fun viewModelClass(): Class<V>

    protected abstract fun viewModelFactory(): T

    protected lateinit var viewModel: V
    protected lateinit var binding: B
    protected lateinit var factory: T

    @LayoutRes
    protected abstract fun layoutResId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelFactory().create(viewModelClass())
        binding = DataBindingUtil.setContentView(this, layoutResId())
        binding.lifecycleOwner = this
        binding.executePendingBindings()
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