package com.klo.example.presentation.splash

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
import com.klo.example.domain.model.*
import com.klo.example.presentation.webview.components.Utils
import org.json.JSONObject


class SplashFragment : Fragment() {

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        Utils().setColorScreen(win = requireActivity().window, context = requireContext())

        if(Utils().isNetworkAvailable(context = requireContext())) {
            initialObservers()
            viewModel.getSharedPref()
        } else openWebView()

        return view
    }
    //
    private fun initialObservers() {
        viewModel.mutableAppInfoLiveData.observe(viewLifecycleOwner, appInfoLiveData())
        viewModel.mutableFacebookLiveData.observe(viewLifecycleOwner, facebookLiveData())
        viewModel.mutableAppsflyerLiveData.observe(viewLifecycleOwner, appsflyerLiveData())
        viewModel.mutableReferrerLiveData.observe(viewLifecycleOwner, referrerLiveData())
        viewModel.mutableFlowLiveData.observe(viewLifecycleOwner, flowLiveData())
        viewModel.mutableIsAppsInitLiveData.observe(viewLifecycleOwner, isAppInit())
        viewModel.mutableGetSharedPrefLiveData.observe(viewLifecycleOwner, getSharedPrefLiveData())
    }
    private fun getSharedPrefLiveData() = Observer<SharedPrefModel> { model->
        if(model.last_url != null)
            openWebView(
                type = "non-organic",
                url = model.last_url!!,
                fullscreen = model.fullscreen!!,
                orientation = model.orientation!!
            )
        else viewModel.getAppInfo()
    }

    private fun appInfoLiveData() = Observer<AppDataModel> { model->
        if(model != null) {
            Log.i("APP_CHECK", "[AppData]: $model")
            AppsFlyerLib.getInstance().init(model.appsflyer, null, requireContext())
            AppsFlyerLib.getInstance().start(requireContext(), model.appsflyer, object :
                AppsFlyerRequestListener {
                override fun onSuccess() {
                    Log.i("APP_CHECK", "[Appsflyer] Инициализирован успешно")
                    viewModel.getFacebook(
                        intent = requireActivity().intent,
                        id = model.fb_app_id,
                        token = model.fb_client_token
                    )
                    viewModel.getAppsflyer()
                    viewModel.getReferrer()
                }

                override fun onError(p0: Int, p1: String) {
                    Log.i("APP_CHECK", "[Appsflyer] Ошибка инициализации: $p0 $p1")
                }
            })

        } else openWebView()
    }

    private fun facebookLiveData() = Observer<FacebookModel> { model->
        if (model != null) {
            if (model.campaign != null) {

            }
            Log.i("APP_CHECK", "[Facebook]: $model")
        } else {
            // TODO: facebook кампания не пришла
        }
    }
    private fun appsflyerLiveData() = Observer<AppsflyerModel> { model->
        if (model != null){
            val t = "3t_nikita_vanya_nasha_russia"
            if (t != "None" && t != null) {
                val jsonObject = JSONObject()
                with(jsonObject) {
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
                //
                viewModel.getFlow(
                    jsonObject = jsonObject,
                    flowkey = t.substringBefore('_')
                )
                Log.i("APP_CHECK", "[Appsflyer]: $model")
            }
        }
    }
    private fun referrerLiveData() = Observer<ReferrerModel> { model->
        if (model != null) {
            Log.i("APP_CHECK", "[Referrer]: $model")
        }
    }
    private fun flowLiveData() = Observer<FlowModel> { model->
        if (model != null) {
            Log.i("APP_CHECK", "[URL Offer]: $model")
            viewModelStore.clear()
            openWebView(
                type = "non-organic",
                url = model.url,
                fullscreen = model.fullscreen,
                orientation = model.orientation
            )
        }
    }
    private fun isAppInit() = Observer<Boolean> { model->
        if (model) {

        } else {
            openWebView()
        }
    }
    //
    private fun openWebView(type: String = "organic", url: String = "", fullscreen: Int = 0, orientation: Int = 0) {
        val bundle = Bundle()
        with(bundle) {
            bundle.putString("url", url)
            bundle.putString("type_join", type)
            bundle.putInt("fullscreen", fullscreen)
            bundle.putInt("orientation", orientation)
        }
        findNavController().navigate(R.id.webViewFragment, bundle)
    }
}