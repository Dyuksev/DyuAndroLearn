package com.dyukov.dyuandrolearn.ui.profile

import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.data.network.UserModel
import com.dyukov.dyuandrolearn.databinding.FragmentProfileBinding
import org.kodein.di.generic.instance

class ProfileFragment :
    BaseFragment<ProfileViewModel, FragmentProfileBinding, ProfileViewModelFactory>() {

    override fun viewModelClass(): Class<ProfileViewModel> = ProfileViewModel::class.java

    override fun viewModelFactory(): ProfileViewModelFactory = ProfileViewModelFactory()

    override fun layoutResId(): Int = R.layout.fragment_profile

    val userModel: UserModel by instance<UserModel>()
}