package com.wisdomegypt.appqd.presentation.webview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.wisdomegypt.appqd.R
import com.wisdomegypt.appqd.obfuscation.Controller
import com.wisdomegypt.appqd.presentation.common.Utils
import com.wisdomegypt.appqd.presentation.webview.components.NonOrganicWV
import com.wisdomegypt.appqd.presentation.webview.components.OrganicWV


class WebViewFragment : Fragment() {
    //
    private val viewModel by viewModels<WebViewViewModel>()
    //
    //
    private lateinit var wv: WebView
    private lateinit var fragmentLayout: FrameLayout
    //
    private lateinit var white: OrganicWV
    private lateinit var main: NonOrganicWV
    //
    private var type : String? = null
    private var fullscreen : Int? = null
    //
    private val IC = 1
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private var mCameraPhotoPath: String? = null
    //
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_web_view, container, false)
        setDefaultColorBar()
        //
        wv = view.findViewById(R.id.webview)
        fragmentLayout = view.findViewById(R.id.target_view)
        //
        white = OrganicWV(webview = wv, context = requireContext(), activity = requireActivity())
        main = NonOrganicWV(webView = wv, context = requireContext(), activity = requireActivity())

        initialObservers()

        type = requireArguments().getString("type_join")
        val url = requireArguments().getString("url")
        fullscreen = requireArguments().getInt("fullscreen", 0)
        val orientation = requireArguments().getInt("orientation", 0)

        if((type == "non-organic" || type == "organic_url") && url != null && Controller().obf()) {
            main.open(
                type = type!!,
                viewModel = viewModel,
                fullscreen = fullscreen!!,
                orientation = orientation,
                url = url,
                fragmentLayout = fragmentLayout
            )
            if(type != "organic_url"){
                viewModel.saveSharedPrefs(
                    url = url,
                    fullscreen = fullscreen,
                    orientation = orientation
                )
            }
        } else white.open()
        return view
    }

    private fun sharedPrefLiveData() = Observer<Boolean> { model->
        if (model && Controller().obf()) {
            Log.i("APP_CHECK", "saveOfferUrlLiveData: сохранено")
            // TODO: сохранена ссылка
        }
    }

    override fun onPause() {
        super.onPause()
        if(type == "non-organic" && Controller().obf()) viewModel.saveSharedPrefs(url = wv.url)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
        return
    }
    private fun initialObservers() {
        viewModel.mutableSaveSharedPrefLiveData.observe(viewLifecycleOwner, sharedPrefLiveData())
    }
    private fun setDefaultColorBar() {
        requireActivity().window.navigationBarColor = requireContext().resources.getColor(R.color.white)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        requireActivity().window.statusBarColor = requireContext().resources.getColor(R.color.white)
    }
    override fun onStart() {
        super.onStart()
        when {
            type == "non-organic" && fullscreen != 1 && Controller().obf() -> Utils().setFull(win = requireActivity().window, false)
            type == "organic" && Controller().obf() -> Utils().setFull(win = requireActivity().window)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModelStore.clear()
    }

}