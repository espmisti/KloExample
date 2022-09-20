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
import com.klo.example.obfuscation.Controller
import com.klo.example.presentation.splash.components.KloJSON
import com.klo.example.presentation.webview.components.Utils
import org.json.JSONObject
import kotlin.coroutines.resume


class SplashFragment : Fragment() {

    private val viewModel by viewModels<SplashViewModel>()

    private val jsonObject = JSONObject()

    private var flowKey : String? = null

    private var fb_id: String? = null
    private var fb_token: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        if(Controller().obf()) Utils().setColorScreen(win = requireActivity().window, context = requireContext())
        if(Controller().obf()) initialObservers()
        if(Controller().obf()) viewModel.internetConnection()
        return view
    }
    private fun initialObservers() {
        // Проверка интернета
        viewModel.mutableInternetLiveData.observe(viewLifecycleOwner, internetLiveData())
        // Получение цифр с сервера
        viewModel.mutableAppInfoLiveData.observe(viewLifecycleOwner, appInfoLiveData())
        // Инициализация фейсбука
        viewModel.mutableInitFacebookLiveData.observe(viewLifecycleOwner, initFacebook())
        //
        viewModel.mutableFacebookLiveData.observe(viewLifecycleOwner, facebookLiveData())
        viewModel.mutableAppsflyerLiveData.observe(viewLifecycleOwner, appsflyerLiveData())
        viewModel.mutableReferrerLiveData.observe(viewLifecycleOwner, referrerLiveData())
        // Получение потока с сервера
        viewModel.mutableFlowLiveData.observe(viewLifecycleOwner, flowLiveData())
        // Сохранение в SharedPreference данные
        viewModel.mutableGetSharedPrefLiveData.observe(viewLifecycleOwner, sharedPreferenceLiveData())
        // Получение данные устройства
        viewModel.mutableSystemLiveData.observe(viewLifecycleOwner, getSystemDataLiveData())
        // Получение ссылки на органику
        viewModel.mutableWebViewLiveData.observe(viewLifecycleOwner, organicURL())
    }
    private fun internetLiveData() = Observer<Boolean> {
        if(it) viewModel.getSharedPref()
        else viewModel.openWebView(type = 2)
    }
    private fun sharedPreferenceLiveData() = Observer<SharedPrefModel> { model->
        if(model.last_url != null && Controller().obf())
            viewModel.openWebView(
                type = 1,
                url = model.last_url!!,
                fullscreen = model.fullscreen!!,
                orientation = model.orientation!!
            )
        else viewModel.getAppInfo()
    }
    private fun appInfoLiveData() = Observer<AppDataModel> { model->
        Log.i("APP_CHECK", "[AppData]: $model")
        AppsFlyerLib.getInstance().init(model.appsflyer, null, requireContext())
        AppsFlyerLib.getInstance().start(requireContext(), model.appsflyer, object : AppsFlyerRequestListener {
            override fun onSuccess() {
                if(Controller().obf()) {
                    Log.i("APP_CHECK", "- Appsflyer инициализирован успешно -")
                    fb_id = model.fb_app_id
                    fb_token = model.fb_client_token
                    viewModel.getAppsflyer()
                }
            }
            override fun onError(p0: Int, p1: String) {
                Log.i("APP_CHECK", "- Appsflyer ошибка инициализации: $p0 $p1 -")
                if(Controller().obf()) viewModel.initialFacebook(id = model.fb_app_id, token = model.fb_client_token, intent = requireActivity().intent)
            }
        })
    }
    private fun appsflyerLiveData() = Observer<AppsflyerModel> { model->
        if (model.campaign != null && model.campaign != "null" && model.campaign != "None" && Controller().obf()) {
            flowKey = model.campaign!!.substringBefore('_')
            KloJSON().getAppsflyer(jsonObject, model)
            //
            viewModel.getSystemData()
        } else {
            viewModel.initialFacebook(id = fb_id!!, token = fb_token!!, intent = requireActivity().intent)
        }
        Log.i("APP_CHECK", "\n\n[Appsflyer]: $model")
    }
    private fun initFacebook() = Observer<Boolean> {
        if(it) viewModel.getFacebook(intent = requireActivity().intent)
        else viewModel.getReferrer()
    }
    private fun facebookLiveData() = Observer<FacebookModel> { model->
        if (model.campaign != null && Controller().obf()) {
            
            flowKey = model.campaign!!.substringBefore('_')
            KloJSON().getFacebook(jsonObject, model)
            //
            viewModel.getSystemData()
        } else {
            if(Controller().obf()) viewModel.getReferrer()
        }
        Log.i("APP_CHECK", "\n\n[Facebook]: $model")
    }
    private fun referrerLiveData() = Observer<ReferrerModel> { model->
        if (model.installReferrer != null && !model.installReferrer!!.contains("utm_source") && !model.installReferrer!!.contains("utm_medium") && Controller().obf()) {
            flowKey = model.installReferrer!!.substringBefore('_')
            KloJSON().getRefferer(jsonObject, model)
            //
            viewModel.getSystemData()
        } else {
            viewModel.openWebView()
        }
        Log.i("APP_CHECK", "\n\n[Referrer]: $model")
    }
    private fun getSystemDataLiveData() = Observer<SystemModel> { model->
        if (model != null && Controller().obf()) {
            KloJSON().getSystem(jsonObject, model)
            viewModel.getFlow(jsonObject = jsonObject, flowkey = flowKey.toString())
            Log.i("APP_CHECK", "object: $jsonObject")
        }
    }
    private fun flowLiveData() = Observer<FlowModel?> { model->
        if (model != null && Controller().obf()) {
            Log.i("APP_CHECK", "[URL Offer]: $model")
            viewModelStore.clear()
            viewModel.openWebView(
                type = 1,
                url = model.url,
                fullscreen = model.fullscreen,
                orientation = model.orientation
            )
        } else {
            Log.i("APP_CHECK", "[FLOW LIVE DATA]: Flowkey не найден")
            viewModel.openWebView()
        }
    }
    private fun organicURL() = Observer<HashMap<String, String>> { model->
        val bundle = Bundle()
        with(bundle) {
            bundle.putString("url", model["url"])
            bundle.putString("type_join", model["type_join"])
            bundle.putInt("fullscreen", model["fullscreen"]!!.toInt())
            bundle.putInt("orientation", model["orientation"]!!.toInt())
        }
        findNavController().navigate(R.id.webViewFragment, bundle)
    }
}