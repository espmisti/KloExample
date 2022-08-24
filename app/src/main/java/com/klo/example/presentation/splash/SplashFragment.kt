package com.klo.example.presentation.splash

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.klo.example.R
import com.klo.example.data.Constants
import com.klo.example.domain.model.*
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject


class SplashFragment : Fragment() {

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        screenSetting(window = requireActivity().window)
        if(isNetworkAvailable(requireContext())) {
            initialObservers()
            viewModel.getLastURL()
        } else findNavController().navigate(R.id.webViewFragment)
        return view
    }
    //
    private fun initialObservers() {
        viewModel.mutableAppInfoLiveData.observe(viewLifecycleOwner, appInfoLiveData())
        viewModel.mutableGetLastURLLiveData.observe(viewLifecycleOwner, lastURLLiveData())
        viewModel.mutableSaveLastURLLiveData.observe(viewLifecycleOwner, saveLastUrlLiveData())
        viewModel.mutableFacebookLiveData.observe(viewLifecycleOwner, facebookLiveData())
        viewModel.mutableAppsflyerLiveData.observe(viewLifecycleOwner, appsflyerLiveData())
        viewModel.mutableReferrerLiveData.observe(viewLifecycleOwner, referrerLiveData())
        viewModel.mutableOfferLiveData.observe(viewLifecycleOwner, offerLiveData())
    }

    private fun saveLastUrlLiveData() = Observer<Boolean> { model->
        if (model) {

        } else {

        }
    }

    private fun appInfoLiveData() = Observer<AppDataModel> { model->
        if(model != null) {
            Log.i("APP_CHECK", "appInfoLiveData: $model")
            AppsFlyerLib.getInstance().init(model.appsflyer, null, requireContext())
            AppsFlyerLib.getInstance().start(requireContext(), model.appsflyer, object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    Log.i("APP_CHECK", "Успешно")
                    viewModel.getFacebook(
                        intent = requireActivity().intent,
                        id = model.fb_app_id,
                        token = model.fb_client_token
                    )
                    viewModel.getAppsflyer(appsflyer_id = model.appsflyer)
                    viewModel.getReferrer()
                }

                override fun onError(p0: Int, p1: String) {
                    Log.i("APP_CHECK", "Ошибка $p0, $p1")
                }
            })

        } else openWebView()
    }
    private fun lastURLLiveData() = Observer<LastURLModel> { model->
        if (model.last_url != null) openWebView("non-organic")
        else viewModel.getAppInfo()
    }
    private fun facebookLiveData() = Observer<FacebookModel> { model->
        if (model != null) {
            if (model.campaign != null) {

            }
            Log.i("APP_CHECK", "facebookLiveData: $model")
        } else {
            // TODO: facebook кампания не пришла
        }
    }
    private fun appsflyerLiveData() = Observer<AppsflyerModel> { model->
        if (model?.campaign != null) {
            val jsonObject = JSONObject()
            val dataJsonObject = JSONObject()
            with(dataJsonObject) {
                put("redirect_response_data", model.redirect_response_data)
                put("adgroup_id", model.adgroup_id)
                put("engmnt_source", model.engmnt_source)
                put("retargeting_conversion_type", model.retargeting_conversion_type)
                put("is_incentivized", model.is_incentivized)
                put("orig_cost", model.orig_cost)
                put("is_first_launch", model.is_first_launch)
                put("af_click_lookback", model.af_click_lookback)
                put("af_cpi", model.af_cpi)
                put("iscache", model.iscache)
                put("click_time", model.click_time)
                put("is_branded_link", model.is_branded_link)
                put("match_type", model.match_type)
                put("adset", model.adset)
                put("campaign_id", model.campaign_id)
                put("install_time", model.install_time)
                put("media_source", model.media_source)
                put("af_sub1", model.af_sub1)
                put("clickid", model.clickid)
                put("af_siteid", model.af_siteid)
                put("af_status", model.af_status)
                put("cost_cents_USD", model.cost_cents_USD)
                put("af_r", model.af_r)
                put("af_sub4", model.af_sub4)
                put("af_sub3", model.af_sub3)
                put("af_sub2", model.af_sub2)
                put("adset_id", model.adset_id)
                put("http_referrer", model.http_referrer)
                put("is_universal_link", model.is_universal_link)
                put("is_retargeting", model.is_retargeting)
                put("adgroup", model.adgroup)
                put("ts", model.ts)
            }
            with(jsonObject) {
                put("token", Constants.API.TOKEN)
                put("flowkey", "3t")
                put("data", dataJsonObject)

            }
            viewModel.getUrlOffer(jsonObject = jsonObject)
            Log.i("APP_CHECK", "appsflyerLiveData: $model")
        }
    }
    private fun referrerLiveData() = Observer<ReferrerModel> { model->
        if (model != null) {
            Log.i("APP_CHECK", "referrerLiveData: $model")
        }
    }
    private fun offerLiveData() = Observer<URLOfferModel> { model->
        if (model != null) {
            viewModel.saveLastUrl(url = model.url)
            Log.i("APP_CHECK", "offerLiveData: $model")
            openWebView("non-organic")
        }
    }
    //
    fun isNetworkAvailable(context: Context): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) return true
        }
        return false
    }
    fun screenSetting(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = resources.getColor(R.color.navigation)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.header)
        }
    }

    private fun openWebView(type: String = "organic") {
        val bundle = Bundle()
        bundle.putString("type_join", type)
        findNavController().navigate(R.id.webViewFragment, bundle)
    }
}