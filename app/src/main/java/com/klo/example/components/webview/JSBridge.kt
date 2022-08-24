package com.klo.example.components.webview

import android.content.Context
import android.webkit.JavascriptInterface

class JSBridge(private val context: Context) {
    private val logTag: String = "JS_BRIDGE"

    @JavascriptInterface
    fun openOffer() {}
}