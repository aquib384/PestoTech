package com.example.pestotech.utils

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.example.pestotech.R


object ProgressBarUtility {
    /**
     * SHOW PROGRESS BAR IN FRAGMENT
     */
    fun getProgressDialog(context: Context): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(0)
        )
        dialog.setContentView(R.layout.dialog_progress)
        dialog.setCancelable(false)
        return dialog
    }
}