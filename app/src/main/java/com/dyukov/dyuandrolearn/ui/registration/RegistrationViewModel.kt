package com.dyukov.dyuandrolearn.ui.registration

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class RegistrationViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth

    val etUsername = MutableLiveData("")
    val etPassword = MutableLiveData("")
}