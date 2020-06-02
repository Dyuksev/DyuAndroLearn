package com.dyukov.dyuandrolearn.ui.login

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.data.network.UserModel
import com.dyukov.dyuandrolearn.databinding.FragmentLoginBinding
import com.dyukov.dyuandrolearn.ui.MainActivity
import com.dyukov.dyuandrolearn.utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance


class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding, LoginViewModelFactory>() {

    override fun layoutResId(): Int = R.layout.fragment_login
    override fun viewModelClass(): Class<LoginViewModel> = LoginViewModel::class.java
    override fun viewModelFactory(): LoginViewModelFactory = LoginViewModelFactory()
    private val mDatabaseReference: DatabaseReference? by instance<DatabaseReference>()
    private val mDatabase: FirebaseDatabase? by instance<FirebaseDatabase>()

    val userModel: UserModel by instance<UserModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setNavBarVisibility(false)
    }

    private fun initView() {
        tv_sign_up.setOnClickListener {
            Utils.hideKeyboard(it)
            findNavController().navigate(R.id.action_login_to_registration)

        }
        bt_sign_in.setOnClickListener {
            Utils.hideKeyboard(it)
            SignIn()
        }
        tv_forgot_pass.setOnClickListener {
            Utils.hideKeyboard(it)
            showResetPasswordDialog()
        }

    }

    private fun SignIn() {
        showProgress()
        GlobalScope.launch {
            mAuth.signInWithEmailAndPassword(
                viewModel.etEmail.value.toString(),
                viewModel.etPassword.value.toString()
            ).addOnCompleteListener(requireActivity(), OnCompleteListener { task ->
                if (task.isSuccessful) {
                    checkIsLogginOn()
                    hideProgress()
                } else {
                    hideProgress()
                    Toast.makeText(requireActivity(), "Login Failed", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun checkIsLogginOn() {
        if (mAuth.currentUser != null)
            findNavController().navigate(R.id.action_login_to_intro_screen)
    }

    private fun sendPasswordResetToEmail(email: String) {
        showProgress()
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity(), OnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireActivity(),
                        "The password reset link was sent! ",
                        Toast.LENGTH_LONG
                    ).show()
                    hideProgress()
                } else {
                    hideProgress()
                    Toast.makeText(
                        requireActivity(),
                        "Unable to send reset mail",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })
    }

    fun showResetPasswordDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle("Enter your email to receive a password reset link")

        val input = EditText(activity)
        input.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        builder.setView(input)


        builder.setPositiveButton("Reset password",
            DialogInterface.OnClickListener { dialog, which ->
                sendPasswordResetToEmail(input.text.toString())
            })
        builder.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }
}