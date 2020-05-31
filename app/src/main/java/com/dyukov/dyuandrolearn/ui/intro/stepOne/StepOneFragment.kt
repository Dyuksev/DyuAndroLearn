package com.dyukov.dyuandrolearn.ui.intro.stepOne

import android.os.Bundle
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.databinding.FragmentStepOneBinding
import com.dyukov.dyuandrolearn.ui.MainActivity
import com.dyukov.dyuandrolearn.ui.intro.stepFour.StepFourFragment

class StepOneFragment :
    BaseFragment<StepOneViewModel, FragmentStepOneBinding, StepOneViewModelFactory>() {
    override fun viewModelClass(): Class<StepOneViewModel> = StepOneViewModel::class.java

    override fun viewModelFactory(): StepOneViewModelFactory = StepOneViewModelFactory()

    override fun layoutResId(): Int = R.layout.fragment_step_one

    companion object {
        fun newInstance(args: Bundle? = null) = StepOneFragment().apply { arguments = args }
    }
}