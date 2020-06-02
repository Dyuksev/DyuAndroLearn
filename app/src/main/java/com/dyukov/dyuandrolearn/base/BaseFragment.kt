package com.dyukov.dyuandrolearn.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dyukov.dyuandrolearn.BR
import com.dyukov.dyuandrolearn.data.db.LessonsRepository
import com.dyukov.dyuandrolearn.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

abstract class BaseFragment<V : BaseViewModel, B : ViewDataBinding, T : ViewModelProvider.NewInstanceFactory>
    : Fragment(), KodeinAware {

    override val kodein by kodein()

    val mAuth: FirebaseAuth by instance<FirebaseAuth>()

    protected abstract fun viewModelClass(): Class<V>

    protected abstract fun viewModelFactory(): T

    protected lateinit var viewModel: V
    protected lateinit var binding: B
    protected lateinit var factory: T

    protected val lessonsRepository by instance<LessonsRepository>()


    @LayoutRes
    protected abstract fun layoutResId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelFactory().create(viewModelClass())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId(), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
    }

    fun showProgress() {
        (activity as MainActivity).showProgress()
    }

    fun hideProgress() {
        (activity as MainActivity).hideProgress()
    }
}