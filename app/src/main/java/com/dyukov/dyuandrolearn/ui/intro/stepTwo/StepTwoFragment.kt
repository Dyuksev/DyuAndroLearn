package com.dyukov.dyuandrolearn.ui.intro.stepTwo

import android.os.Bundle
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.databinding.FragmentStepTwoBinding
import com.dyukov.dyuandrolearn.ui.MainActivity

class StepTwoFragment :
    BaseFragment<StepTwoViewModel, FragmentStepTwoBinding, StepTwoViewModelFactory>() {

    override fun viewModelClass(): Class<StepTwoViewModel> = StepTwoViewModel::class.java

    override fun viewModelFactory(): StepTwoViewModelFactory = StepTwoViewModelFactory()

    override fun layoutResId(): Int = R.layout.fragment_step_two

    companion object {
        fun newInstance(args: Bundle? = null) = StepTwoFragment().apply { arguments = args }
    }
}