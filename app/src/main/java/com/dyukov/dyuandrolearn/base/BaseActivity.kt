package com.dyukov.dyuandrolearn.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

abstract class BaseActivity<V : BaseViewModel, B : ViewDataBinding, T : ViewModelProvider.NewInstanceFactory> :
    AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

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
        binding  = DataBindingUtil.setContentView(this, layoutResId())
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }

}