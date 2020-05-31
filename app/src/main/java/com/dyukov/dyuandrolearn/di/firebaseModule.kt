package com.dyukov.dyuandrolearn.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

const val MODULE_NAME = "Firebase Module"

val firebaseModule = Kodein.Module(MODULE_NAME, false) {
    bind() from singleton { FirebaseAuth.getInstance() }
    bind() from singleton { FirebaseDatabase.getInstance() }
    bind() from singleton { FirebaseDatabase.getInstance().getReference("users") }
}