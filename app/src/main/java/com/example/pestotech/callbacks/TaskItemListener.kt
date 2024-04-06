package com.example.pestotech.callbacks

import android.view.View
import com.example.pestotech.model.Task

interface TaskItemListener<T> {
    fun onTaskAdded(task:Task)
}