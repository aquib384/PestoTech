package com.example.pestotech.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pestotech.model.Task
import com.example.pestotech.service.NetworkResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(

    private val reference: DatabaseReference, private val mAuth: FirebaseAuth

) : ViewModel() {

    private val _task = MutableLiveData<NetworkResult<List<Task>>>()
    val task: LiveData<NetworkResult<List<Task>>>
        get() = _task


    fun addTask(task: Task) = viewModelScope.launch {
        val id = reference.push().key
        val uid = mAuth.uid
        id?.let {
            task.taskId = it
            reference.child(uid!!).child(id).setValue(task)
        }

    }

    fun updateTask(task: Task) = viewModelScope.launch {
        reference.child(mAuth.uid!!).child(task.taskId).setValue(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        reference.child(mAuth.uid!!).child(task.taskId).removeValue()
    }

    fun getTask() = viewModelScope.launch {
        _task.value = NetworkResult.Loading()
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val taskList = arrayListOf<Task>()

                for (i in snapshot.children) {
                    for (j in i.children) {
                        val id = j.key.toString()
                        val title = j.child(TITLE).value.toString()
                        val description = j.child(DESCRIPTION).value.toString()
                        val status = j.child(STATUS).value.toString()
                        if (i.key == mAuth.uid) {
                            taskList.add(Task(id, title, description, status))
                        }
                    }

                }
                _task.value = NetworkResult.Success(taskList)

            }

            override fun onCancelled(error: DatabaseError) {
                _task.value = NetworkResult.Error(error.toString())

            }
        })
    }

    companion object {
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val STATUS = "status"
    }

}