package com.dyukov.dyuandrolearn.ui.profile

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.data.network.UserModel
import com.dyukov.dyuandrolearn.databinding.FragmentProfileBinding
import com.dyukov.dyuandrolearn.ui.MainActivity
import com.dyukov.dyuandrolearn.utils.PreferenceStorage
import kotlinx.android.synthetic.main.fragment_profile.*
import org.kodein.di.generic.instance

class ProfileFragment :
    BaseFragment<ProfileViewModel, FragmentProfileBinding, ProfileViewModelFactory>() {

    override fun viewModelClass(): Class<ProfileViewModel> = ProfileViewModel::class.java

    override fun viewModelFactory(): ProfileViewModelFactory = ProfileViewModelFactory()

    override fun layoutResId(): Int = R.layout.fragment_profile

    private val preferenceStorage: PreferenceStorage? by instance<PreferenceStorage>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_logout.setOnClickListener {
            mAuth.signOut()
            preferenceStorage?.clearIsDataLoaded()
            findNavController().navigate(R.id.action_profile_to_login)
            (activity as MainActivity).resetBottomMenu()

        }
    }
}