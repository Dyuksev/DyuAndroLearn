package com.dyukov.dyuandrolearn.ui.registration

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.data.db.network.UserModel
import com.dyukov.dyuandrolearn.databinding.FragmentRegisterBinding
import com.dyukov.dyuandrolearn.ui.MainActivity
import com.dyukov.dyuandrolearn.utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance


class RegistrationFragment :
    BaseFragment<RegistrationViewModel, FragmentRegisterBinding, RegistrationViewModelFactory>() {

    override fun layoutResId(): Int = R.layout.fragment_register
    override fun viewModelClass(): Class<RegistrationViewModel> = RegistrationViewModel::class.java
    override fun viewModelFactory(): RegistrationViewModelFactory = RegistrationViewModelFactory()
    val mAuth: FirebaseAuth by instance<FirebaseAuth>()

    private val mDatabaseReference: DatabaseReference? by instance<DatabaseReference>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    fun initView() {
        bt_sign_up.setOnClickListener {
            createUser()
            Utils.hideKeyboard(it)
        }

        iv_back.setOnClickListener {
            Utils.hideKeyboard(it)
            requireActivity().onBackPressed()
        }
    }

    fun createUser() {
        showProgress()
        val email = viewModel.etEmail.value.toString()
        val username = viewModel.etPassword.value.toString()
        GlobalScope.launch {
            mAuth.createUserWithEmailAndPassword(
                email, username
            ).addOnCompleteListener(requireActivity(), OnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    user?.uid?.let {
                        saveDataIntoFirebaseDatabase(it, username, email)
                    }
                } else {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_LONG)
                        .show()
                }
            })
        }
    }

    fun saveDataIntoFirebaseDatabase(uId: String, username: String, email: String) {
        val userModel = UserModel()
        userModel.email = email
        userModel.username = username
        mDatabaseReference?.child(uId)?.setValue(userModel)
        hideProgress()
        Toast.makeText(context, "Successfully Registered", Toast.LENGTH_LONG)
            .show()
        requireActivity().onBackPressed()
    }


}