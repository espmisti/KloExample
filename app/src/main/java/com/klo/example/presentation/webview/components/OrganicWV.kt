package com.klo.example.presentation.webview.components

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.telephony.TelephonyManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import com.klo.example.R
import com.klo.example.data.repository.InstallLogDataRepository
import com.klo.example.domain.usecase.SendInstallLogUseCase
import com.klo.example.obfuscation.Controller
import com.klo.example.presentation.common.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class OrganicWV(private val webview: WebView, private val context: Context, private val activity: Activity) {
    private val TAG = "APP_CHECK_ORGANIC"
    fun open () {
        initWebView()
        CoroutineScope(Dispatchers.IO).launch {
            if (Utils().isInternetEnabled(context = context, tag = "nope")) {
                if(Controller().obf()) SendInstallLogUseCase(installLogRepository = InstallLogDataRepository(pkg = context.packageName, tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager, join_type = "organic")).execute()
            }
        }
        Log.i(TAG, "[WebView]: Открыт вайт")
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(){
        webview.background = context.getDrawable(R.drawable.bg)
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