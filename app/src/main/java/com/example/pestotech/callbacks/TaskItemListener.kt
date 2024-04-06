package com.example.pestotech.callbacks

import com.example.pestotech.model.Task

interface TaskItemListener<T> {
    fun onTaskAdded(task: Task)
}