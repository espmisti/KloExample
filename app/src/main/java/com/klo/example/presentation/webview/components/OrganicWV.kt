package com.klo.example.presentation.webview.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat

class OrganicWV(private val webview: WebView) {
    fun open (win: Window, context: Context, activity: Activity) {
        Utils().setFull(win = win)
        initWebView(context = context, activity = activity)
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(context: Context, activity: Activity){
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
        webview.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    activity.finish()
                    return true
                } else return false
            }
        })
    }
    private class LocalContentWebViewClient(private val assetLoader: WebViewAssetLoader) : WebViewClientCompat() {
        override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(request.url)
        }
    }
    private class JSBridge() {
        @JavascriptInterface
        fun openOffer() {}
    }
}