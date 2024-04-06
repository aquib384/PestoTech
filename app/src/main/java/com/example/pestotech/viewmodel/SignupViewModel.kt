package com.example.pestotech.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pestotech.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SignupViewModel @Inject constructor(
    private val mAuth: FirebaseAuth,
    private val reference:DatabaseReference

    ) : ViewModel() {

    var signeup = MutableLiveData<Boolean>()

    fun createUser(name:String, email: String, password: String) : LiveData<Boolean>{
        mAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
               signeup.value = task.isSuccessful
                val mEmail = mAuth.currentUser?.email
                val uid = mAuth.uid
                reference.child("users").child(mAuth.uid.toString()).setValue(User.Users(name,mEmail,uid))
            }

        return signeup
    }


}