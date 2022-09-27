package com.wisdomegypt.appqd.presentation.splash

import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.wisdomegypt.appqd.R
import com.wisdomegypt.appqd.data.repository.SystemDataRepository
import com.wisdomegypt.appqd.domain.model.*
import com.wisdomegypt.appqd.domain.usecase.GetSystemInfoUseCase
import com.wisdomegypt.appqd.presentation.common.Utils
import com.wisdomegypt.appqd.presentation.splash.common.KloJSON
import org.json.JSONObject


class SplashFragment : Fragment() {

    private val viewModel by viewModels<SplashViewModel>()
    private val TAG = "APP_CHECK_SPLASH"

    private val jsonObject = JSONObject()
    private var flowKey: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        // Настройка экрана
        Utils().setColorScreen(win = requireActivity().window, context = requireContext())
        // Инициализация обсерверов
        initialObservers()
        // Проверка интернета
        if (Utils().isInternetEnabled(context = requireContext(), tag = TAG)){
            // Получение последней ссылки из памяти
            val data = Utils().getSharedPrefs(context = requireContext())
            if (data.last_url != null) viewModel.openWebView(type = 1, url = data.last_url!!, fullscreen = data.fullscreen!!, orientation = data.orientation!!)
            else initialAppsflyer()
        } else {
            viewModel.openWebView()
        }
        return view
    }
    private fun initialObservers() {
        // Получение цифр с сервера
        viewModel.appSuccessLiveData.observe(viewLifecycleOwner, appSuccessLiveData())
        viewModel.appFailureLiveData.observe(viewLifecycleOwner, appFailureLiveData())
        // Инициализация Facebook
        viewModel.initFacebookSuccessLiveData.observe(viewLifecycleOwner, initFacebookSuccess())
        viewModel.initFacebookFailureLiveData.observe(viewLifecycleOwner, initFacebookFailure())
        // Получение Facebook
        viewModel.facebookSuccessLiveData.observe(viewLifecycleOwner, facebookSuccessLiveData())
        viewModel.facebookFailureLiveData.observe(viewLifecycleOwner, facebookFailureLiveData())
        // Получение Referrer
        viewModel.referrerSuccessLiveData.observe(viewLifecycleOwner, referrerSuccessLiveData())
        viewModel.referrerFailureLiveData.observe(viewLifecycleOwner, referrerFailureLiveData())
        // Получение токена для пушей
        viewModel.pushTokenLiveData.observe(viewLifecycleOwner, pushToken())
        // Получение Flow данных
        viewModel.flowSuccessLiveData.observe(viewLifecycleOwner, flowSuccessLiveData())
        viewModel.flowFailureLiveData.observe(viewLifecycleOwner, flowFailureLiveData())
        // Получение ссылки на органику
        viewModel.mutableWebViewLiveData.observe(viewLifecycleOwner, organicURL())
    }

    private fun initialAppsflyer() {
        AppsFlyerLib.getInstance().init(getString(R.string.af_dev_key), null, requireActivity().application)
        AppsFlyerLib.getInstance().start(requireActivity().application, getString(R.string.af_dev_key), object : AppsFlyerRequestListener {
            override fun onSuccess() {
                val listener = object : AppsFlyerConversionListener {
                    override fun onConversionDataSuccess(conversionData: MutableMap<String, Any>?) {
                        Log.i(TAG, "[Appsflyer]: $conversionData")
                        conversionData?.let {
                            if(it["campaign"] != null && it["campaign"] != "None" && it["campaign"] != "null") {
                                flowKey = it["campaign"].toString().substringBefore("_")
                                KloJSON().getAppsflyer(jsonObject = jsonObject, it)
                                viewModel.getPushToken()
                            } else viewModel.initialAppData()
                        } ?: run {
                            viewModel.initialAppData()
                        }

                    }

                    override fun onConversionDataFail(p0: String?) {
                        Log.i("APP_CHECK", "onConversionDataSuccess: ")

                    }

                    override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                        Log.i("APP_CHECK", "onConversionDataSuccess: ")

                    }

                    override fun onAttributionFailure(p0: String?) {
                        Log.i("APP_CHECK", "onConversionDataSuccess: ")

                    }
                }
                AppsFlyerLib.getInstance().registerConversionListener(requireActivity().application, listener)
                Log.i(TAG, "[Appsflyer]: Успешно инициализирован")
            }
            override fun onError(p0: Int, p1: String) {
                Log.e(TAG, "[Appsflyer]: $p1 - $p0")
                viewModel.initialAppData()
            }
        })
    }
    // Получение с сервера данных о приложении
    private fun appSuccessLiveData() = Observer<AppDataModel> { model->
        Log.i(TAG, "[APP DATA]: Успешно (${model.fb_app_id} | ${model.fb_client_token})")
        viewModel.initialFacebook(id = model.fb_app_id, token = model.fb_client_token, intent = requireActivity().intent)
    }
    private fun appFailureLiveData() = Observer<String> { errorMessage->
        Log.e(TAG, errorMessage)
        viewModel.openWebView(type = 2)
    }
    // Инициализация Facebook
    private fun initFacebookSuccess() = Observer<Boolean> { model->
        Log.i(TAG, "[Facebook]: Успешно инициализирован")
        viewModel.getFacebook(intent = requireActivity().intent)
    }
    private fun initFacebookFailure() = Observer<String> { errorMessage->
        Log.e(TAG, errorMessage)
        viewModel.getReferrer()
    }
    // Получение данных с Facebook
    private fun facebookSuccessLiveData() = Observer<ArrayList<String>>{
        flowKey = it[0]
        KloJSON().getFacebook(jsonObject = jsonObject, it[1])
        viewModel.getPushToken()
    }
    private fun facebookFailureLiveData() = Observer<String> { errorMessage->
        Log.e(TAG, errorMessage)
        viewModel.getReferrer()
    }
    // Получение данных с Referrer
    private fun referrerSuccessLiveData() = Observer<HashMap<String, String>> {
        flowKey = it["installReferrer"].toString().substringAfter("&c=").substringBefore('_')
        KloJSON().getRefferer(jsonObject = jsonObject, it)
        viewModel.getPushToken()
    }
    private fun referrerFailureLiveData() = Observer<String> { errorMessage->
        Log.e(TAG, errorMessage)
        viewModel.openWebView(type = 2)
    }
    // Получение пуш токена
    private fun pushToken() = Observer<String> {
        jsonObject.put("fcm_token", it)
        Log.i(TAG, "[TOKEN]: $it")
        getSystemData(flowkey = flowKey!!)
    }
    // Получение данных устройства
    private fun getSystemData(flowkey: String) {
        val tm : TelephonyManager = requireActivity().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        KloJSON().getSystem(jsonObject = jsonObject, GetSystemInfoUseCase(systemRepository = SystemDataRepository(tm = tm, pkg = requireContext().packageName)).execute())
        viewModel.getFlow(jsonObject = jsonObject, flowkey = flowkey, tm = tm)
    }
    // Получение Flow
    private fun flowSuccessLiveData() = Observer<FlowModel> {
        Log.i(TAG, "[FlowKey]: $it")
        viewModel.openWebView(type = 1, url = it.url, fullscreen = it.fullscreen, orientation = it.orientation)
    }
    private fun flowFailureLiveData() = Observer<String> { errorMessage->
        Log.e(TAG, errorMessage)
        viewModel.openWebView(type = 2)
    }
    // Откытие WebView
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
    override fun onDestroyView() {
        super.onDestroyView()
        viewModelStore.clear()
    }
}