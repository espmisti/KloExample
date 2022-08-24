package com.klo.example.presentation.webview.components

import android.webkit.*

class WV {
    fun initWebView (wv: WebView) {
        val ws: WebSettings = wv.settings
        //
        ws.javaScriptCanOpenWindowsAutomatically = true
        ws.pluginState = WebSettings.PluginState.ON
        //
        ws.allowFileAccess = true
        ws.loadWithOverviewMode = true
        ws.builtInZoomControls = true
        ws.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        ws.useWideViewPort = true
        ws.loadWithOverviewMode = true
        ws.savePassword = true
        ws.saveFormData = true
        ws.javaScriptEnabled = true
        ws.domStorageEnabled = true
        wv.isSaveEnabled = false
        ws.saveFormData = false
        //
        ws.javaScriptEnabled = true
        ws.setSupportZoom(false)
        //
    }
}