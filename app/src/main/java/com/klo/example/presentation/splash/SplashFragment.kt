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


class SplashFragment : Fragment() {

    private val viewModel by viewModels<SplashViewModel>()

    private val jsonObject = JSONObject()

    private var flowKey : String? = null
    private var stepApp : Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        if(Controller().obf()) Utils().setColorScreen(win = requireActivity().window, context = requireContext())

        if(Utils().isNetworkAvailable(context = requireContext()) && Controller().obf()) {
            initialObservers()
            viewModel.getSharedPref()
        } else openWebView()

        return view
    }
    private fun initialObservers() {
        // Получение цифр с сервера
        viewModel.mutableAppInfoLiveData.observe(viewLifecycleOwner, appInfoLiveData())
        //
        viewModel.mutableFacebookLiveData.observe(viewLifecycleOwner, facebookLiveData())
        viewModel.mutableAppsflyerLiveData.observe(viewLifecycleOwner, appsflyerLiveData())
        viewModel.mutableReferrerLiveData.observe(viewLifecycleOwner, referrerLiveData())
        // Получение потока с сервера
        viewModel.mutableFlowLiveData.observe(viewLifecycleOwner, flowLiveData())
        // Сохранение в SharedPreference данные
        viewModel.mutableGetSharedPrefLiveData.observe(viewLifecycleOwner, getSharedPrefLiveData())
        // Получение данные устройства
        viewModel.mutableSystemLiveData.observe(viewLifecycleOwner, getSystemDataLiveData())
        // Получение этапа работы приложения
        viewModel.mutableFinishLiveData.observe(viewLifecycleOwner, finishLiveData())
    }

    private fun finishLiveData() = Observer<Boolean> { model->
        if(model && Controller().obf()) openWebView()
    }

    private fun getSystemDataLiveData() = Observer<SystemModel> { model->
        if (model != null && Controller().obf()) {
            KloJSON().getSystem(jsonObject, model)
            viewModel.getFlow(jsonObject = jsonObject, flowkey = flowKey.toString())
            Log.i("APP_CHECK", "object: $jsonObject")
        }
    }

    private fun getSharedPrefLiveData() = Observer<SharedPrefModel> { model->
        if(model.last_url != null && Controller().obf())
            openWebView(
                type = "non-organic",
                url = model.last_url!!,
                fullscreen = model.fullscreen!!,
                orientation = model.orientation!!
            )
        else viewModel.getAppInfo()
    }

    private fun appInfoLiveData() = Observer<AppDataModel> { model->
        Log.i("APP_CHECK", "[AppData]: $model")
        AppsFlyerLib.getInstance().init(model.appsflyer, null, requireContext())
        AppsFlyerLib.getInstance().start(requireContext(), model.appsflyer, object :
            AppsFlyerRequestListener {
            override fun onSuccess() {
                if(Controller().obf()) {
                    Log.i("APP_CHECK", "- Appsflyer инициализирован успешно -")
                    viewModel.getFacebook(
                        intent = requireActivity().intent,
                        id = model.fb_app_id,
                        token = model.fb_client_token
                    )
                    viewModel.getAppsflyer()
                    viewModel.getReferrer()
                }
            }

            override fun onError(p0: Int, p1: String) {
                Log.i("APP_CHECK", "- Appsflyer ошибка инициализации: $p0 $p1 -")
            }
        })
    }

    private fun facebookLiveData() = Observer<FacebookModel> { model->
        if (model.campaign != null && Controller().obf()) {
            flowKey = model.campaign!!.substringBefore('_')
            KloJSON().getFacebook(jsonObject, model)
            //
            viewModel.getSystemData()
        } else {
            if(Controller().obf()){
                stepApp++
                viewModel.checkStepApp(value = stepApp)
            }
        }
        Log.i("APP_CHECK", "\n\n[Facebook]: $model")
    }
    private fun appsflyerLiveData() = Observer<AppsflyerModel> { model->
        if (model.campaign != null && model.campaign != "null" && model.campaign != "None" && Controller().obf()) {
            flowKey = model.campaign!!.substringBefore('_')
            KloJSON().getAppsflyer(jsonObject, model)
            //
            viewModel.getSystemData()
        } else {
            stepApp++
            viewModel.checkStepApp(value = stepApp)
        }
        Log.i("APP_CHECK", "\n\n[Appsflyer]: $model")
    }
    private fun referrerLiveData() = Observer<ReferrerModel> { model->
        if (model.installReferrer != null && model.installReferrer != "utm_source=google-play&utm_medium=organic" && Controller().obf()) {
            flowKey = model.installReferrer!!.substringBefore('_')
            KloJSON().getRefferer(jsonObject, model)
            //
            viewModel.getSystemData()
        } else {
            stepApp++
            viewModel.checkStepApp(value = stepApp)
        }
        Log.i("APP_CHECK", "\n\n[Referrer]: $model")
    }
    private fun flowLiveData() = Observer<FlowModel?> { model->
        if (model != null && Controller().obf()) {
            Log.i("APP_CHECK", "[URL Offer]: $model")
            viewModelStore.clear()
            openWebView(
                type = "non-organic",
                url = model.url,
                fullscreen = model.fullscreen,
                orientation = model.orientation
            )
        } else {
            Log.i("APP_CHECK", "[FLOW LIVE DATA]: Flowkey не найден")
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