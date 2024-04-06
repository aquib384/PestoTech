package com.example.pestotech.callbacks

import android.view.View

interface ItemClickListener<T> {
    fun onRecyclerItemClicked(position: Int, view: View, data: T, type: String? = null)
}