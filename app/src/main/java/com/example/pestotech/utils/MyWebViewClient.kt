package com.bhadohi.fitpeo.utils


import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.pestotech.utils.ProgressBarUtility
import javax.inject.Inject


class MyWebViewClient @Inject constructor(
    private val context: Context)
    : WebViewClient() {
    private  var progressDialog: Dialog = ProgressBarUtility.getProgressDialog(context = context)


    override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
        view?.loadUrl(url)
        return true
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
       handler?.proceed()
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        progressDialog.show()
        progressDialog.setCancelable(true)

    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        progressDialog.dismiss()
    }


}