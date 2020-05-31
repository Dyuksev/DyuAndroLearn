package com.dyukov.dyuandrolearn.ui.login

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.dyukov.dyuandrolearn.R
import com.dyukov.dyuandrolearn.base.BaseFragment
import com.dyukov.dyuandrolearn.data.db.network.UserModel
import com.dyukov.dyuandrolearn.databinding.FragmentLoginBinding
import com.dyukov.dyuandrolearn.utils.Utils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance
import timber.log.Timber


class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding, LoginViewModelFactory>() {

    override fun layoutResId(): Int = R.layout.fragment_login
    override fun viewModelClass(): Class<LoginViewModel> = LoginViewModel::class.java
    override fun viewModelFactory(): LoginViewModelFactory = LoginViewModelFactory()
    val mAuth: FirebaseAuth by instance<FirebaseAuth>()
    private val mDatabaseReference: DatabaseReference? by instance<DatabaseReference>()
    private val mDatabase: FirebaseDatabase? by instance<FirebaseDatabase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        mAuth.currentUser?.let {

            mDatabaseReference?.child(it.uid)?.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val user: UserModel? = p0.getValue(UserModel::class.java)

                }
            })

        }
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

    fun SignIn() {
        showProgress()
        GlobalScope.launch {
            mAuth.signInWithEmailAndPassword(
                viewModel.etEmail.value.toString(),
                viewModel.etPassword.value.toString()
            ).addOnCompleteListener(requireActivity(), OnCompleteListener { task ->
                if (task.isSuccessful) {
                    checkisLogginOn()
                    hideProgress()
                } else {
                    hideProgress()
                    Toast.makeText(requireActivity(), "Login Failed", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    fun checkisLogginOn() {

    }

    fun sendPasswordResetToEmail(email: String) {
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