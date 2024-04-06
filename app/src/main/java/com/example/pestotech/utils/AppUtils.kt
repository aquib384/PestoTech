package com.example.pestotech.utils

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Utils class
 */

fun isValidEmail(email: String?): Boolean {
    return !email.isNullOrBlank() && !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(
        email
    ).matches()
}

fun isValidPasswordFormat(password: String?): Boolean {
    val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
    val pattern: Pattern = Pattern.compile(passwordPattern)
    val matcher: Matcher = pattern.matcher(password)
    return matcher.matches()
}

// show showToast
fun Context.showToast(msg: String?) {
    if (!msg.isNullOrEmpty()) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}




