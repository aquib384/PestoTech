package com.example.pestotech.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val mAuth: FirebaseAuth

    ) : ViewModel() {

    private var login = MutableLiveData<Boolean>()

    fun isLogin(email: String, password: String): LiveData<Boolean> {
        mAuth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                login.value = task.isSuccessful

            }

        return login
    }


}