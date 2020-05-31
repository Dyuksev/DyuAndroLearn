package com.dyukov.dyuandrolearn.ui.registration

import android.app.Activity
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.dyukov.dyuandrolearn.base.BaseViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegistrationViewModel : BaseViewModel() {


    val etUsername = MutableLiveData("")
    val etEmail = MutableLiveData("")
    val etPassword = MutableLiveData("")
    val etPasswordRepeat = MutableLiveData("")

    val isPasswordValid = MediatorLiveData<Boolean>()
    val isEmailValid = Transformations.map(etEmail) {
        isValidEmail(etEmail.value.toString())
    }
    val isUserNameValid = Transformations.map(etUsername) {
        etUsername.value.toString().length > 3
    }
    val isButtonEnabled = MediatorLiveData<Boolean>()

    init {
        val observers = Observer<Any> {
            isPasswordValid.value =
                etPassword.value == etPasswordRepeat.value && etPassword.value!!.length > 5 && etPasswordRepeat.value!!.length > 5
        }

        isPasswordValid.addSource(etPassword, observers)
        isPasswordValid.addSource(etPasswordRepeat, observers)

        val buttonObserver = Observer<Any> {
            isButtonEnabled.value =
                isPasswordValid.value == true && isEmailValid.value == true && isUserNameValid.value == true
        }

        isButtonEnabled.addSource(isPasswordValid, buttonObserver)
        isButtonEnabled.addSource(isEmailValid, buttonObserver)
        isButtonEnabled.addSource(isUserNameValid, buttonObserver)
    }

    fun isValidEmail(email: String?) =
        !email.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
