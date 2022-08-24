package com.klo.example.presentation.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.FrameLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import com.klo.example.R
import com.klo.example.components.webview.ImageLoader
import com.klo.example.components.webview.JSBridge
import com.klo.example.domain.model.LastURLModel
import com.klo.example.presentation.webview.components.WV
import java.io.File
import java.io.IOException


class WebViewFragment : Fragment() {
    //
    private val viewModel by viewModels<WebViewViewModel>()

    //
    private lateinit var wv: WebView
    private var mCustomView: View? = null
    private var fragmentLayout: FrameLayout? = null
    private lateinit var mCustomViewCallback : WebChromeClient.CustomViewCallback
    //
    private val IC = 1
    private val FR = 1
    private var mUploadMessage: ValueCallback<Uri>? = null
    private var mCapturedImageURI: Uri? = null
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var mCameraPhotoPath: String? = null
    //
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_web_view, container, false)
        //
        wv = view.findViewById(R.id.webview)
        fragmentLayout = view.findViewById(R.id.target_view)
        //
        initialObservers()
        viewModel.getURLOffer()
        //
        if(requireArguments().getString("type_join") == "non-organic") getOfferUrlLiveData()
        else openWhiteURL()
        return view
    }
    private fun initialObservers() {
        viewModel.mutableGetLastURLLiveData.observe(viewLifecycleOwner, getOfferUrlLiveData())
        viewModel.mutableSaveLastURLLiveData.observe(viewLifecycleOwner, saveOfferUrlLiveData())
    }

    private fun saveOfferUrlLiveData() = Observer<Boolean> { model->
        if (model) {
            Log.i("APP_CHECK", "saveOfferUrlLiveData: ссылка сохранена")
            // TODO: сохранена ссылка
        } else {
            // TODO: ошибка сохранения
        }
    }

    private fun getOfferUrlLiveData() = Observer<LastURLModel> { model->
        if (model != null) {
            Log.i("APP_CHECK", "LAST_URL: ${model.last_url}")
            if(model.last_url != null) {
                initialWebView()
                wv.loadUrl(model.last_url!!)
            } else {
                Log.i("APP_CHECK", "onCreateView: БЛЯЯЯЯЯЯЯЯТь")
                openWhiteURL()
            }
        }
    }
    private fun openWhiteURL() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        WindowInsetsControllerCompat(requireActivity().window, wv).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        loadApplication()
    }
    private fun initialWebView() {
        WV().initWebView(wv = wv)
        wv.webChromeClient = webChromeClient(context = requireContext())
        wv.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK && event?.action === MotionEvent.ACTION_UP && wv.canGoBack()) {
                    wv.goBack()
                    return true
                } else return false
            }
        })
        wv.webViewClient = webViewClient()
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun loadApplication(){
        val u = "https://appassets.androidplatform.net/assets/index.html"
        wv.settings.javaScriptEnabled = true
        wv.settings.allowFileAccess = true
        wv.settings.loadWithOverviewMode = true
        wv.settings.domStorageEnabled = true
        wv.settings.useWideViewPort = true
        wv.addJavascriptInterface(JSBridge(requireContext()), "JSBridge")
        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(requireContext()))
            .addPathHandler("/res/", WebViewAssetLoader.ResourcesPathHandler(requireContext()))
            .build()
        wv.webViewClient = LocalContentWebViewClient(assetLoader)
        wv.loadUrl(u)
        wv.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK && event?.action === MotionEvent.ACTION_UP && wv.canGoBack()) {
                    wv.goBack()
                    return true
                } else {
                    return false
                }
            }
        })
    }
    private class LocalContentWebViewClient(private val assetLoader: WebViewAssetLoader) : WebViewClientCompat() {
        override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(request.url)
        }
    }
    override fun onPause() {
        super.onPause()
        wv.evaluateJavascript("javascript:window.ApplicationSoundOff()", null)
        if(requireArguments().getString("type_join") == "non-ogranic") viewModel.saveURLOffer(wv.url)
    }
    override fun onResume() {
        super.onResume()
        wv.evaluateJavascript("javascript:window.ApplicationSoundOn()", null)

        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        WindowInsetsControllerCompat(requireActivity().window, wv).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != IC || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            var results: Array<Uri>? = null
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    if (mCameraPhotoPath != null) {
                        results = arrayOf(Uri.parse(mCameraPhotoPath))
                    }
                } else {
                    val dataString = data.dataString
                    if (dataString != null) {
                        results = arrayOf(Uri.parse(dataString))
                    }
                }
            }
            mFilePathCallback!!.onReceiveValue(results)
            mFilePathCallback = null
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FR || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data)
                return
            }
            if (requestCode == FR) {
                if (null == mUploadMessage) {
                    return
                }
                var result: Uri? = null
                try {
                    result = if (resultCode != Activity.RESULT_OK) {
                        null
                    } else {
                        if (data == null) mCapturedImageURI else data.data
                    }
                } catch (e: Exception) {
                }
                mUploadMessage!!.onReceiveValue(result)
                mUploadMessage = null
            }
        }
        return
    }
    private fun webChromeClient(context: Context) = object: WebChromeClient() {
        override fun onShowFileChooser(view: WebView, filePath: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams): Boolean {
            mFilePathCallback?.onReceiveValue(null)
            mFilePathCallback = filePath
            var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent!!.resolveActivity(context.applicationContext.packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = ImageLoader().createImageFile(context.applicationContext)
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
            startActivityForResult(chooserIntent, 1)
            return true
        }
        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
            if (mCustomView != null) {
                callback?.onCustomViewHidden()
                return
            }
            mCustomView = view
            wv.visibility = View.GONE
            fragmentLayout?.visibility = View.VISIBLE
            fragmentLayout?.addView(view)
            mCustomViewCallback = callback!!
            setFull(win = requireActivity().window)
        }

        override fun onHideCustomView() {
            if (mCustomView == null) return

            wv.visibility = View.VISIBLE
            fragmentLayout?.visibility = View.VISIBLE
            mCustomView!!.visibility = View.GONE

            fragmentLayout?.removeView(mCustomView)
            mCustomViewCallback.onCustomViewHidden()
            mCustomView = null
        }
    }
    private fun setFull(win: Window){
        wi n.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) win.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        val decorView: View = win.decorView
        val uiOptions: Int = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        decorView.systemUiVisibility = uiOptions
    }
    private fun webViewClient() = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return if (url == null || url.startsWith("http://") || url.startsWith("https://")) false else try {
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
                if (html != "\"\\u003Chtml>\\u003Chead>\\u003C/head>\\u003Cbody>\\u003C/body>\\u003C/html>\"") viewModel.saveURLOffer(wv.url)
                else {
                    Log.i("APP_CHECK", "onCreateView: Чеее бля")
                    openWhiteURL()
                }
            }
        }
    }

    override fun onDestroy() {
        retainInstance = true
    }
}