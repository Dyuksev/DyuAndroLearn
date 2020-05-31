package com.dyukov.dyuandrolearn.ui.intro.stepThree

import android.os.Bundle
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.databinding.FragmentStepThreeBinding
import com.dyukov.dyuandrolearn.ui.MainActivity

class StepThreeFragment :
    BaseFragment<StepThreeViewModel, FragmentStepThreeBinding, StepThreeViewModelFactory>() {

    override fun viewModelClass(): Class<StepThreeViewModel> = StepThreeViewModel::class.java

    override fun viewModelFactory(): StepThreeViewModelFactory = StepThreeViewModelFactory()

    override fun layoutResId(): Int = R.layout.fragment_step_three


    companion object {
        fun newInstance(args: Bundle? = null) = StepThreeFragment().apply { arguments = args }
    }
}