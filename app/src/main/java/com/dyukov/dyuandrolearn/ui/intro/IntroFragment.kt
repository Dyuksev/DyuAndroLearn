package com.dyukov.dyuandrolearn.ui.intro

import android.os.Bundle
import android.view.View
import androidx.annotation.Px
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.databinding.FragmentIntroBinding
import com.dyukov.dyuandrolearn.ui.MainActivity
import com.dyukov.dyuandrolearn.ui.intro.adapter.FragmentsPagerAdapter
import com.dyukov.dyuandrolearn.ui.intro.stepFour.StepFourFragment
import com.dyukov.dyuandrolearn.ui.intro.stepOne.StepOneFragment
import com.dyukov.dyuandrolearn.ui.intro.stepThree.StepThreeFragment
import com.dyukov.dyuandrolearn.ui.intro.stepTwo.StepTwoFragment
import kotlinx.android.synthetic.main.fragment_intro.*

class IntroFragment : BaseFragment<IntroViewModel, FragmentIntroBinding, IntroViewModelFactory>() {

    var adapter: FragmentsPagerAdapter? = null

    override fun viewModelClass(): Class<IntroViewModel> = IntroViewModel::class.java

    override fun viewModelFactory(): IntroViewModelFactory = IntroViewModelFactory()

    override fun layoutResId(): Int = R.layout.fragment_intro

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPager()
        initViews()

    }


    private fun initViews() {
        bt_next.setOnClickListener {
            viewModel.currentPosition.apply {
                if (viewModel.currentPosition == 2)
                    findNavController().navigate(R.id.action_intro_to_home)
                else
                    vp_intro?.currentItem = viewModel.currentPosition + 1
            }

        }
    }

    private fun setPager() {
        if (adapter == null) {
            adapter = FragmentsPagerAdapter(childFragmentManager)
            adapter?.apply {
                addFragment(StepOneFragment.newInstance())
                addFragment(StepTwoFragment.newInstance())
                addFragment(StepThreeFragment.newInstance())
            }
        }
        vp_intro.apply {
            adapter = this@IntroFragment.adapter
            tab_layout?.setupWithViewPager(this, true)
            tab_layout?.getTabAt(0)?.select()
            offscreenPageLimit = 1
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    @Px positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    viewModel.currentPosition = position
                    if (position == 2)
                        bt_next.text = "Go"
                    else bt_next.text = "Next"
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
        }

    }

}