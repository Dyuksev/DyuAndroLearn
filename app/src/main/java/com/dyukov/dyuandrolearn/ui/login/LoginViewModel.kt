package com.dyukov.dyuandrolearn.ui.login

import android.util.Patterns
import androidx.lifecycle.*
import com.dyukov.dyuandrolearn.base.BaseViewModel

class LoginViewModel : BaseViewModel() {

    val etEmail = MutableLiveData("")
    val etPassword = MutableLiveData("")
    val isPasswordValid = Transformations.map(etPassword) {
        etPassword.value!!.length > 5
    }
    val isButtonEnabled = MediatorLiveData<Boolean>()
    val isEmailValid = Transformations.map(etEmail) {
        isValidEmail(etEmail.value.toString())
    }

    init {
        val buttonObserver = Observer<Any> {
            isButtonEnabled.value =
                isPasswordValid.value == true && isEmailValid.value == true
        }

        isButtonEnabled.addSource(isEmailValid, buttonObserver)
        isButtonEnabled.addSource(isPasswordValid, buttonObserver)
    }

    fun isValidEmail(email: String?) =
        !email.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}