package com.klo.example.presentation.webview.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.net.http.SslError
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.FrameLayout
import com.klo.example.presentation.webview.WebViewViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class NonOrganicWV(private val webView: WebView, private val context: Context, private val activity: Activity) {
    //
    private val white = OrganicWV(webview = webView, context = context, activity = activity)
    //
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var mCameraPhotoPath: String? = null
    //
    private var mCustomView: View? = null
    private lateinit var mCustomViewCallback : WebChromeClient.CustomViewCallback

    fun open (viewModel: WebViewViewModel, fullscreen: Int, orientation: Int, url: String, fragmentLayout: FrameLayout){
        initWebView(url = url, viewModel = viewModel, fullscreen = fullscreen, fragmentLayout = fragmentLayout)
        //
        if(fullscreen == 1) Utils().setFull(win = activity.window, "non-organic")
        if(orientation == 1) activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView (url: String, viewModel: WebViewViewModel, fullscreen: Int, fragmentLayout: FrameLayout) {
        val ws: WebSettings = webView.settings
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
        ws.domStorageEnabled = true
        webView.isSaveEnabled = false
        ws.saveFormData = false
        //
        ws.javaScriptEnabled = true
        ws.setSupportZoom(false)
        //
        webView.webChromeClient = if(fullscreen == 2) webChromeFullScreen(fragmentLayout = fragmentLayout) else webChromeClient()
        webView.webViewClient = webViewClient(viewModel = viewModel)
        webView.loadUrl(url)
        webView.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                    webView.goBack()
                    return true
                } else return false
            }
        })

    }
    private fun webViewClient(viewModel: WebViewViewModel) = object : WebViewClient() {
        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return if (url.startsWith("http://") || url.startsWith("https://")) false else try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
                true
            } catch (e: Exception) {
                Log.i("TAG", "shouldOverrideUrlLoading Exception:$e")
                true
            }
        }
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            view.evaluateJavascript("(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();") { html: String ->
                if (html == "\"\\u003Chtml>\\u003Chead>\\u003C/head>\\u003Cbody>\\u003C/body>\\u003C/html>\"")
                    white.open()
                else viewModel.saveSharedPrefs(url = url)
            }
        }
    }

    private fun webChromeFullScreen(fragmentLayout: FrameLayout) = object : WebChromeClient() {
        override fun onShowFileChooser(view: WebView, filePath: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams): Boolean {
            showFileChooser(filePath)
            return true
        }
        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
            if (mCustomView != null) {
                callback?.onCustomViewHidden()
                return
            }
            mCustomView = view
            webView.visibility = View.GONE
            fragmentLayout?.visibility = View.VISIBLE
            fragmentLayout?.addView(view)
            mCustomViewCallback = callback!!
            Utils().setFull(win = activity.window)
        }
        override fun onHideCustomView() {
            if (mCustomView == null) return

            webView.visibility = View.VISIBLE
            fragmentLayout?.visibility = View.VISIBLE
            mCustomView!!.visibility = View.GONE

            fragmentLayout?.removeView(mCustomView)
            mCustomViewCallback.onCustomViewHidden()
            mCustomView = null
        }
    }

    private fun webChromeClient() = object: WebChromeClient() {
        override fun onShowFileChooser(view: WebView, filePath: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams): Boolean {
            showFileChooser(filePath)
            return true
        }
    }
    @SuppressLint("SimpleDateFormat")
    fun createImageFile(context: Context): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        )
        var imageFile: File? = null
        try {
            imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return imageFile
    }
    private fun showFileChooser(filePath: ValueCallback<Array<Uri>>) {
        mFilePathCallback?.onReceiveValue(null)
        mFilePathCallback = filePath
        var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent!!.resolveActivity(context.applicationContext.packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImageFile(context.applicationContext)
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath)
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.absolutePath
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
            } else takePictureIntent = null
        }
        val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
        contentSelectionIntent.type = "image/*"
        val intentArray: Array<Intent?> = takePictureIntent?.let { arrayOf(it) } ?: arrayOfNulls(0)
        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        chooserIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
        activity.startActivityForResult(chooserIntent, 1)
    }

}