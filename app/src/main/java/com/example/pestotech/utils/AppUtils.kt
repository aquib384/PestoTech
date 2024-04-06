package com.example.pestotech.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Utils class
 */


fun getFirstName(name:String): String{

    val firstName = name.split(" ")


    return firstName[0]

}

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


 fun getImageUri(context: Context, inImage: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(
        context.contentResolver,
        inImage,
        "Title",
        null
    )
    return Uri.parse(path)
}

/**
 * Hide keyboard
 */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Format date
 */
fun Context.formatDate(str: String): String {

    val dateArray = str.split("-".toRegex())
    var date = ""

    for (i in dateArray.indices.reversed()) {

        date += if (i > 0) {
            dateArray[i] + "-"
        } else {
            dateArray[i]

        }

    }

    return date
}



