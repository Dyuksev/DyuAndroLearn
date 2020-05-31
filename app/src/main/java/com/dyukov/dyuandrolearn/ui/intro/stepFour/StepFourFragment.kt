package com.dyukov.dyuandrolearn.ui.intro.stepFour

import android.os.Bundle
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.databinding.FragmentStepFourBinding

class StepFourFragment :
    BaseFragment<StepFourViewModel, FragmentStepFourBinding, StepFourViewModelFactory>() {

    override fun viewModelClass(): Class<StepFourViewModel> = StepFourViewModel::class.java

    override fun viewModelFactory(): StepFourViewModelFactory = StepFourViewModelFactory()

    override fun layoutResId(): Int = R.layout.fragment_step_four

    companion object {
        fun newInstance(args: Bundle? = null) = StepFourFragment().apply { arguments = args }
    }
}