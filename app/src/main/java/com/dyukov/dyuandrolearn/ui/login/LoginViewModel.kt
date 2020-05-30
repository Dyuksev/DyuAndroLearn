package com.dyukov.dyuandrolearn.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth

    val etUsername = MutableLiveData<String>()
    val etPassword = MutableLiveData<String>()
}