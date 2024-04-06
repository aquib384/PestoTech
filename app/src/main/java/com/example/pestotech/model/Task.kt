package com.example.pestotech.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    var taskId: String = "",
    var title: String = "",
    val description: String = "",
    val status: String = ""
) : Parcelable
