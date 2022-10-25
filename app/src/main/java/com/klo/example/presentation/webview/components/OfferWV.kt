package com.klo.example.presentation.webview.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.telephony.TelephonyManager
import android.view.*
import android.webkit.*
import android.widget.FrameLayout
import com.klo.example.R
import com.klo.example.data.repository.InstallLogDataRepository
import com.klo.example.domain.usecase.SendInstallLogUseCase
import com.klo.example.obfuscation.Controller
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class OfferWV(private val webView: WebView, private val context: Context, private val activity: Activity) {
    //
    private val white = OrganicWV(webview = webView, context = context, activity = activity)
    //
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var mCameraPhotoPath: String? = null
    //
    private var mCustomView: View? = null
    private var mCustomViewCallback : WebChromeClient.CustomViewCallback? = null


    fun open (fullscreen: Int, orientation: Int, url: String, fragmentLayout: FrameLayout){
        initWebView(url = url, fullscreen = fullscreen, fragmentLayout = fragmentLayout)
        //
        CoroutineScope(Dispatchers.IO).launch {
            if(Controller().obf()) SendInstallLogUseCase(installLogRepository = InstallLogDataRepository(pkg = context.packageName, tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager, join_type = "non-organic")).execute()
        }
        if(orientation == 1) activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView (url: String, fullscreen: Int, fragmentLayout: FrameLayout) {
        val ws: WebSettings = webView.settings

        ws.javaScriptEnabled = true     // Поддержка JS
        ws.allowFileAccess = true       // Даем доступ к файлам
        ws.loadWithOverviewMode = true  // Режим обзора, уменьшает масштаб содержимого
        ws.useWideViewPort = true       // Масштаб содержимого
        ws.builtInZoomControls = true   // Возможность масштабировать страницу
        ws.domStorageEnabled = true     // DOM хранилище

        webView.background = context.getDrawable(R.drawable.bg) // Заменяет белый фон у WebView (фикс мерцания)

        webView.webChromeClient = if(fullscreen == 2) webChromeFullScreen(fragmentLayout = fragmentLayout) else webChromeClient()
        webView.webViewClient = WebClient(white = white, context = context)
        webView.loadUrl(url)
        webView.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                webView.goBack()
                true
            } else false
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
            fragmentLayout.visibility = View.VISIBLE
            fragmentLayout.addView(view)
            mCustomViewCallback = callback!!
        }
        override fun onHideCustomView() {
            if (mCustomView == null) return

            webView.visibility = View.VISIBLE
            fragmentLayout.visibility = View.VISIBLE
            mCustomView!!.visibility = View.GONE

            fragmentLayout.removeView(mCustomView)
            mCustomViewCallback?.onCustomViewHidden()
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