package com.klo.example.presentation.webview.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.KeyEvent
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.appcompat.content.res.AppCompatResources
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import com.klo.example.R
import com.klo.example.presentation.common.Utils

class OrganicWV(
    private val webview: WebView,
    private val context: Context,
    private val activity: Activity
) {

    private val TAG = "APP_CHECK_ORGANIC"

    fun open() {
        Utils().setFull(win = activity.window)
        initWebView()
        Log.i(TAG, "[WebView]: Открыт вайт")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webview.background = AppCompatResources.getDrawable(context, R.drawable.bg)
        webview.settings.javaScriptEnabled = true
        webview.settings.allowFileAccess = true
        webview.settings.loadWithOverviewMode = true
        webview.settings.domStorageEnabled = true
        webview.settings.useWideViewPort = true
        webview.addJavascriptInterface(JSBridge(), "JSBridge")
        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(context))
            .addPathHandler("/res/", WebViewAssetLoader.ResourcesPathHandler(context))
            .build()
        webview.webViewClient = LocalContentWebViewClient(assetLoader)
        webview.loadUrl("https://appassets.androidplatform.net/assets/index.html")
        webview.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                activity.finish()
                true
            } else false
        }
    }

    private class LocalContentWebViewClient(private val assetLoader: WebViewAssetLoader) :
        WebViewClientCompat() {
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(request.url)
        }
    }
    // Надо решить с этим что-то
    private class JSBridge() {
        @JavascriptInterface
        fun openOffer() {}
    }
}