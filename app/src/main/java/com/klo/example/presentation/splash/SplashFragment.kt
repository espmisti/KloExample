package com.klo.example.presentation.splash

import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.klo.example.R
import com.klo.example.app.App
import com.klo.example.data.repository.SystemRepositoryImpl
import com.klo.example.domain.model.*
import com.klo.example.domain.usecase.GetSystemInfoUseCase
import com.klo.example.obfuscation.Controller
import com.klo.example.presentation.common.Utils
import com.klo.example.presentation.splash.common.KloJSON
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject


class SplashFragment : Fragment() {
    private val TAG = "APP_CHECK_SPLASH"
    private val jsonObject = JSONObject()

    @Inject
    lateinit var vmFactory: SplashViewModelFactory
    private lateinit var viewModel: SplashViewModel

    private var flowKey: String? = null

    //
    private var stepToken = 0
    private var stepAdv = 0
    //

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        (requireContext().applicationContext as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, vmFactory)[SplashViewModel::class.java]
        // Настройка экрана
        if(Controller().obf()) Utils().setFull(win = requireActivity().window)
        // Инициализация обсерверов
        if(Controller().obf()) initialObservers()
        // Проверка интернета
        if (Utils().isInternetEnabled(context = requireContext(), tag = TAG) && Controller().obf()){
            // Получение последней ссылки из памяти
            val data = Utils().getSharedPrefs(context = requireContext())
            if (data.last_url != null) viewModel.openWebView(type = 1, url = data.last_url!!, fullscreen = data.fullscreen!!, orientation = data.orientation!!)
            else initialAppsflyer()
        } else {
            if(Controller().obf()) viewModel.openWebView()
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
        viewModel.pushTokenSuccessLiveData.observe(viewLifecycleOwner, pushTokenSuccessLiveData())
        viewModel.pushTokenFailureLiveData.observe(viewLifecycleOwner, pushTokenFailureLiveData())
        // Получение Flow данных
        viewModel.flowSuccessLiveData.observe(viewLifecycleOwner, flowSuccessLiveData())
        viewModel.flowFailureLiveData.observe(viewLifecycleOwner, flowFailureLiveData())
        // Получение ид рекламы
        viewModel.advSuccessLiveData.observe(viewLifecycleOwner, advSuccessLiveData())
        viewModel.advFailureLiveData.observe(viewLifecycleOwner, advFailureLiveData())
        // Получение ссылки на органику
        viewModel.mutableWebViewLiveData.observe(viewLifecycleOwner, organicURL())
    }

    private fun advFailureLiveData() = Observer<String> { errorMessage->
        if(stepAdv == 0) {
            Log.e(TAG, "$errorMessage | Повторяем получение")
            if(Controller().obf()) viewModel.getPushToken()
        } else {
            Log.e(TAG, "$errorMessage | Окончательно его нету пидрила")
        }

    }

    private fun advSuccessLiveData() = Observer<AdvertData> { model->
        Log.i(TAG, "[ADV]: $model")
        with(jsonObject) {
            put("af_id", model.af_id)
            put("ad_id", model.ad_id)
        }
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
                                if(Controller().obf()) flowKey = it["campaign"].toString().substringBefore("_")
                                if(Controller().obf()) KloJSON().getAppsflyer(jsonObject = jsonObject, it)
                                viewModel.getPushToken()
                            } else viewModel.getAppData()
                        } ?: run {
                            if(Controller().obf()) viewModel.getAppData()
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
                if(Controller().obf()) viewModel.getAppData()
            }
        })
    }
    // Получение с сервера данных о приложении
    private fun appSuccessLiveData() = Observer<AppData> { model->
        Log.i(TAG, "[APP DATA]: Успешно (${model.fb_app_id} | ${model.fb_client_token})")
        if(model.fb_app_id != null && model.fb_client_token != null) {
            if(Controller().obf()) viewModel.initialFacebook(id = model.fb_app_id!!, token = model.fb_client_token!!)
        } else viewModel.getReferrer()
    }
    private fun appFailureLiveData() = Observer<String> { errorMessage->
        Log.e(TAG, errorMessage)
        if(Controller().obf()) viewModel.openWebView(type = 2)
    }
    // Инициализация Facebook
    private fun initFacebookSuccess() = Observer<Boolean> {
        Log.i(TAG, "[Facebook]: Успешно инициализирован")
        if(Controller().obf()) viewModel.getFacebook()
    }
    private fun initFacebookFailure() = Observer<String> { errorMessage->
        Log.e(TAG, errorMessage)
        if(Controller().obf()) viewModel.getReferrer()
    }
    // Получение данных с Facebook
    private fun facebookSuccessLiveData() = Observer<ReadyData>{ model ->
        viewModel.getAdv()
        if(Controller().obf()) flowKey = model.flowkey
        if(Controller().obf()) KloJSON().getFacebook(jsonObject = jsonObject, model.campaign)
        if(Controller().obf()) viewModel.getPushToken()
    }
    private fun facebookFailureLiveData() = Observer<String> { errorMessage->
        Log.e(TAG, errorMessage)
        if(Controller().obf()) viewModel.getReferrer()
    }
    // Получение данных с Referrer
    private fun referrerSuccessLiveData() = Observer<ReadyData> { model ->
        viewModel.getAdv()
        if(Controller().obf()) flowKey = model.flowkey
        if(Controller().obf()) KloJSON().getRefferer(jsonObject = jsonObject, model.campaign)
        if(Controller().obf()) viewModel.getPushToken()
    }
    private fun referrerFailureLiveData() = Observer<String> { errorMessage->
        Log.e(TAG, errorMessage)
        if(Controller().obf()) viewModel.openWebView(type = 2)
    }
    // Получение пуш токена
    private fun pushTokenSuccessLiveData() = Observer<String> {
        Log.i(TAG, "[TOKEN]: $it")
        if(Controller().obf()) jsonObject.put("fcm_token", it)
        if(Controller().obf()) getSystemData(flowkey = flowKey!!)
    }
    private fun pushTokenFailureLiveData() = Observer<String> { errorMessage->
        if(stepToken == 0) {
            Log.e(TAG, "$errorMessage | Повторяем получение")
            if(Controller().obf()) viewModel.getPushToken()
            if(Controller().obf()) stepToken++
        } else {
            Log.e(TAG, "$errorMessage | Не получен окончательно")
            if(Controller().obf()) getSystemData(flowkey = flowKey!!)
        }
    }
    // Получение данных устройства
    private fun getSystemData(flowkey: String) {
        val tm : TelephonyManager = requireActivity().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if(Controller().obf()) KloJSON().getSystem(jsonObject = jsonObject, GetSystemInfoUseCase(systemRepository = SystemRepositoryImpl(tm = tm, pkg = requireContext().packageName)).execute(),  af_dev_key = resources.getString(R.string.af_dev_key))
        if(Controller().obf()) viewModel.getFlow(flowkey = flowkey)
    }
    // Получение Flow
    private fun flowSuccessLiveData() = Observer<FlowModel> {
        Log.i(TAG, "[FlowKey]: $it")
        if(Controller().obf()) viewModel.openWebView(type = 1, url = it.url, fullscreen = it.fullscreen, orientation = it.orientation)
    }
    private fun flowFailureLiveData() = Observer<String> { errorMessage->
        Log.e(TAG, errorMessage)
        if(Controller().obf()) viewModel.openWebView(type = 2)
    }
    // Откытие WebView
    private fun organicURL() = Observer<HashMap<String, String>> { model->
        val bundle = Bundle()
        with(bundle) {
            putString("url", model["url"])
            putString("type_join", model["type_join"])
            putInt("fullscreen", model["fullscreen"]?.toInt() ?: 0)
            putInt("orientation", model["orientation"]?.toInt() ?: 1)
        }
        if(Controller().obf()) findNavController().navigate(R.id.webViewFragment, bundle)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        viewModelStore.clear()
    }
}