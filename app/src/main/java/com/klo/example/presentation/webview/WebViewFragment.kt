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
import com.klo.example.obfuscation.Controller
import com.klo.example.presentation.webview.components.OfferWV
import com.klo.example.presentation.webview.components.OrganicWV


class WebViewFragment : Fragment() {

    private lateinit var wv: WebView
    private lateinit var fragmentLayout: FrameLayout
    //
    private var white: OrganicWV? = null
    private var main: OfferWV? = null
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
        //
        wv = view.findViewById(R.id.webview)
        fragmentLayout = view.findViewById(R.id.target_view)
        //
        if(Controller().obf()) white = OrganicWV(webview = wv, context = requireContext(), activity = requireActivity())
        if(Controller().obf()) main = OfferWV(webView = wv, context = requireContext(), activity = requireActivity())

        type = requireArguments().getString("type_join")
        val url = requireArguments().getString("url")
        fullscreen = requireArguments().getInt("fullscreen", 0)
        val orientation = requireArguments().getInt("orientation", 0)

        if((type == "non-organic" || type == "organic_url") && url != null && Controller().obf()) {
            main?.open(
                fullscreen = fullscreen!!,
                orientation = orientation,
                url = url,
                fragmentLayout = fragmentLayout
            )
        } else white?.open()
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != IC || mFilePathCallback == null) {
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
}