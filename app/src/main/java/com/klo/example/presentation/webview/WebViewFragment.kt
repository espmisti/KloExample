package com.klo.example.presentation.webview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.klo.example.R
import com.klo.example.domain.model.SharedPrefModel
import com.klo.example.presentation.webview.components.NonOrganicWV
import com.klo.example.presentation.webview.components.OrganicWV
import com.klo.example.presentation.webview.components.Utils


class WebViewFragment : Fragment() {
    //
    private val viewModel by viewModels<WebViewViewModel>()
    //
    private lateinit var wv: WebView
    private lateinit var fragmentLayout: FrameLayout
    //
    private lateinit var white: OrganicWV
    private lateinit var main: NonOrganicWV
    //
    private var type : String? = null
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
        white = OrganicWV(webview = wv)
        main = NonOrganicWV(webView = wv)
        //
        if (Utils().isNetworkAvailable(context = requireContext())) {
            initialObservers()
            //
            type = requireArguments().getString("type_join")
            val url = requireArguments().getString("url")
            val fullscreen = requireArguments().getInt("fullscreen", 0)
            val orientation = requireArguments().getInt("orientation", 0)

            if(type == "non-organic" && url != null) {
                main.open(
                    win = requireActivity().window,
                    context = requireContext(),
                    activity = requireActivity(),
                    viewModel = viewModel,
                    fullscreen = fullscreen,
                    orientation = orientation,
                    url = url
                )
                viewModel.saveSharedPrefs(
                    url = url,
                    fullscreen = fullscreen,
                    orientation = orientation
                )
            } else white.open(
                win = requireActivity().window,
                context = requireContext(),
                activity = requireActivity()
            )
        } else white.open(
            win = requireActivity().window,
            context = requireContext(),
            activity = requireActivity()
        )
        return view
    }

    private fun sharedPrefLiveData() = Observer<Boolean> { model->
        if (model) {
            Log.i("APP_CHECK", "saveOfferUrlLiveData: сохранено")
            // TODO: сохранена ссылка
        }
    }

    override fun onPause() {
        super.onPause()
        if(type == "non-ogranic")
            viewModel.saveSharedPrefs(url = wv.url)
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
        if(!Utils().isNetworkAvailable(context = requireContext()))
            Utils().setFull(win = requireActivity().window)
        if(type == "non-organic")
            Utils().setFull(win = requireActivity().window, "non-organic")
    }

}